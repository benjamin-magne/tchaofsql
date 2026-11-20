package net.unotec.app.commands.pg.defaults

import net.unotec.app.commands.CommandContext
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters

@Command(mixinStandardHelpOptions = true, name = "defaults", description = ["Valeurs explicites pour les colonnes PostgreSQL sans correspondance ; sinon dÃ©faut SQL ou NULL"])
class PgDefaultsCommands(private val ctx: CommandContext) {
    @Command(mixinStandardHelpOptions = true, name = "show")
    fun show() = println(ctx.data.defaultValues)

    @Command(mixinStandardHelpOptions = true, name = "add")
    fun add(@Parameters(index = "0", paramLabel = "PG_COLUMN") column: String,
            @Parameters(index = "1", paramLabel = "VALUE") value: String) {
        require(ctx.data.postgreColumns.any { it.name == column }) { "Colonne PostgreSQL inconnue." }
        require(ctx.session.getMappedColumns().none { it.postgreColumn.name == column }) { "Colonne dÃ©jÃ  associÃ©e au XML." }
        ctx.data.defaultValues = ctx.data.defaultValues + (column to value)
        ctx.invalidateRows()
    }

    @Command(mixinStandardHelpOptions = true, name = "remove")
    fun remove(@Parameters(paramLabel = "PG_COLUMN") column: String) {
        require(column in ctx.data.defaultValues) { "Aucune valeur par dÃ©faut pour cette colonne." }
        ctx.data.defaultValues = ctx.data.defaultValues - column
        ctx.invalidateRows()
    }
}
