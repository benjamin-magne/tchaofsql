package net.unotec.app.commands.map.pk

import net.unotec.app.commands.CommandContext
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters

@Command(mixinStandardHelpOptions = true, name = "pk", description = ["Colonne XML utilisÃ©e comme clÃ© de conflit PostgreSQL"])
class MapPkCommands(private val ctx: CommandContext) {
    @Command(mixinStandardHelpOptions = true, name = "show")
    fun show() = println(ctx.data.primaryKey ?: "Aucune clÃ© dÃ©finie.")

    @Command(mixinStandardHelpOptions = true, name = "add")
    fun add(@Parameters(paramLabel = "XML_COLUMN") xml: String) {
        ctx.data.primaryKey = ctx.mapper.setPkColumn(xml, ctx.session.getMappedColumns())
    }

    @Command(mixinStandardHelpOptions = true, name = "remove")
    fun remove() { ctx.data.primaryKey = null }
}
