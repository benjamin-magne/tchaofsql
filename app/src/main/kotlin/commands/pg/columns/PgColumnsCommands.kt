package net.unotec.app.commands.pg.columns

import net.unotec.app.commands.CommandContext
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import net.unotec.app.commands.pg.jdbc.metadataConnection

@Command(mixinStandardHelpOptions = true, name = "columns", description = ["MÃ©tadonnÃ©es PostgreSQL"])
class PgColumnsCommands(private val ctx: CommandContext) {
    private fun read(name: String? = null) = ctx.session.getConfiguration().let { config ->
        ctx.database.checkConfiguration(config)
        ctx.database.connect(config).use { ctx.reader.getColumnsMetadata(metadataConnection(it), config, name) }
    }

    @Command(mixinStandardHelpOptions = true, name = "load")
    fun load() {
        val columns = read()
        require(columns.isNotEmpty()) { "Aucune colonne trouvÃ©e pour cette table." }
        ctx.data.postgreColumns = columns
        ctx.resetMappings()
        ctx.data.defaultValues = emptyMap()
        show()
    }

    @Command(mixinStandardHelpOptions = true, name = "show")
    fun show() { ctx.data.postgreColumns.forEach { println("${it.name}: ${it.postgreType}, nullable=${it.isNullable}") } }

    @Command(mixinStandardHelpOptions = true, name = "add")
    fun add(@Parameters(paramLabel = "COLUMN") column: String) {
        val metadata = read(column).singleOrNull { it.name == column } ?: error("Colonne PostgreSQL inconnue : $column")
        ctx.data.postgreColumns = ctx.data.postgreColumns.filter { it.name != column } + metadata
    }

    @Command(mixinStandardHelpOptions = true, name = "remove")
    fun remove(@Parameters(paramLabel = "COLUMN") column: String) {
        require(ctx.data.postgreColumns.any { it.name == column }) { "Colonne PostgreSQL inconnue." }
        ctx.data.postgreColumns = ctx.data.postgreColumns.filter { it.name != column }
        val removed = ctx.session.getMappedColumns().filter { it.postgreColumn.name == column }
        ctx.session.setMappedColumns(ctx.session.getMappedColumns() - removed.toSet())
        if (removed.any { it.xmlColumn == ctx.data.primaryKey }) ctx.data.primaryKey = null
        ctx.data.defaultValues = ctx.data.defaultValues - column
        ctx.invalidateRows()
    }
}
