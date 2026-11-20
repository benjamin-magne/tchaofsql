package net.unotec.app.commands.pg.execute

import net.unotec.app.commands.CommandContext
import net.unotec.app.commands.pg.jdbc.jdbcProxy
import net.unotec.app.models.MappedColumn
import net.unotec.app.services.SqlWriterService
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.sql.Connection
import java.sql.PreparedStatement
import java.util.concurrent.Callable

@Command(name = "execute", mixinStandardHelpOptions = true,
    description = ["UPSERT transactionnel ; rollback par défaut. Conversion validée par PostgreSQL."])
class PgExecuteCommand(private val ctx: CommandContext) : Callable<Int> {
    @Option(names = ["--commitiferror"], description = ["Valider les lignes réussies même si certaines échouent"])
    private var commitIfError = false

    @Option(names = ["--nocommitiferror"], description = ["Valider uniquement si toutes les lignes réussissent"])
    private var noCommitIfError = false

    @Option(names = ["--rollback"], description = ["Tout annuler après vérification"])
    private var rollback = false

    override fun call(): Int {
        require(listOf(commitIfError, noCommitIfError, rollback).count { it } <= 1) { "Les options d'exÃ©cution sont exclusives." }
        val mappings = ctx.session.getMappedColumns()
        require(mappings.isNotEmpty()) { "DÃ©finir les correspondances avec map columns add." }
        val xmlPk = ctx.data.primaryKey ?: error("DÃ©finir la clÃ© avec map pk add.")
        ctx.mapper.setPkColumn(xmlPk, mappings)
        val pgPk = mappings.single { it.xmlColumn == xmlPk }.postgreColumn.name
        require(ctx.data.xmlRecords.isNotEmpty()) { "Charger les enregistrements XML." }
        val defaults = ctx.data.defaultValues.map { (name, _) ->
            MappedColumn("", ctx.data.postgreColumns.single { it.name == name })
        }
        val allMappings = mappings + defaults
        val rows = ctx.data.xmlRecords.map { row ->
            require(!row[xmlPk].isNullOrBlank()) { "ClÃ© primaire XML absente ou vide." }
            ctx.mapper.mapRows(row, mappings) + defaults.associate { it.postgreColumn to ctx.data.defaultValues.getValue(it.postgreColumn.name) }
        }
        ctx.session.setMappedRows(rows)
        val config = ctx.session.getConfiguration()
        ctx.database.checkConfiguration(config)
        val dryRun = rollback || (!commitIfError && !noCommitIfError)
        var succeeded = 0
        var failures = 0
        ctx.database.connect(config).use { connection ->
            connection.autoCommit = false
            // The writer commits each call. Defer those boundaries to the whole import.
            lateinit var transaction: Connection
            transaction = jdbcProxy<Connection>(connection) { method, args ->
                if ((method.name == "commit" || method.name == "rollback") && args.isEmpty()) null
                else if (method.name == "prepareStatement") {
                    val statement = method.invoke(connection, *args) as PreparedStatement
                    jdbcProxy<PreparedStatement>(statement) { m, a ->
                        if (m.name == "getConnection") transaction else m.invoke(statement, *a)
                    }
                } else method.invoke(connection, *args)
            }
            val sql = SqlWriterService(transaction, config)
            try {
                rows.forEachIndexed { index, row ->
                    val savepoint = connection.setSavepoint()
                    try {
                        sql.prepareUpsertStatement(allMappings, row, pgPk).use {
                            ctx.writer.executeUpsertStatement(transaction, it, false)
                        }
                        succeeded++
                    } catch (e: Exception) {
                        connection.rollback(savepoint)
                        failures++
                        System.err.println("Ligne ${index + 1}: ${e.message}")
                    } finally { connection.releaseSavepoint(savepoint) }
                }
                if (dryRun || (failures > 0 && !commitIfError)) connection.rollback() else connection.commit()
            } catch (e: Exception) {
                connection.rollback()
                throw e
            }
        }
        val outcome = if (dryRun || (failures > 0 && !commitIfError)) "transaction annulÃ©e" else "transaction validÃ©e"
        println("$succeeded rÃ©ussite(s), $failures erreur(s) ; $outcome.")
        return if (failures == 0) 0 else 1
    }
}
