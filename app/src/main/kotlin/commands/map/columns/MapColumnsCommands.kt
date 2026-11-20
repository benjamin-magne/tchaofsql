package net.unotec.app.commands.map.columns

import net.unotec.app.commands.CommandContext
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters

@Command(mixinStandardHelpOptions = true, name = "columns", description = ["Correspondances XML vers PostgreSQL"])
class MapColumnsCommands(private val ctx: CommandContext) {
    @Command(mixinStandardHelpOptions = true, name = "show")
    fun show() {
        ctx.session.getMappedColumns().forEach { println("${it.xmlColumn} -> ${it.postgreColumn.name}") }
    }

    @Command(mixinStandardHelpOptions = true, name = "add")
    fun add(@Parameters(index = "0", paramLabel = "XML_COLUMN") xml: String,
            @Parameters(index = "1", paramLabel = "PG_COLUMN") pg: String) {
        require(xml in ctx.data.xmlColumns) { "Colonne XML inconnue : $xml" }
        val target = ctx.data.postgreColumns.singleOrNull { it.name == pg }
            ?: error("Colonne PostgreSQL inconnue : $pg")
        val mapping = ctx.mapper.mapColumn(xml, target)
        require(ctx.session.getMappedColumns().none { it.postgreColumn.name == pg && it.xmlColumn != xml }) {
            "Cette colonne PostgreSQL est dÃ©jÃ  associÃ©e."
        }
        ctx.session.setMappedColumns(ctx.session.getMappedColumns().filter { it.xmlColumn != xml } + mapping)
        ctx.data.defaultValues = ctx.data.defaultValues - pg
        ctx.invalidateRows()
    }

    @Command(mixinStandardHelpOptions = true, name = "remove")
    fun remove(@Parameters(paramLabel = "XML_COLUMN") xml: String) {
        require(ctx.session.getMappedColumns().any { it.xmlColumn == xml }) { "Correspondance inconnue." }
        ctx.session.setMappedColumns(ctx.session.getMappedColumns().filter { it.xmlColumn != xml })
        if (ctx.data.primaryKey == xml) ctx.data.primaryKey = null
        ctx.invalidateRows()
    }
}
