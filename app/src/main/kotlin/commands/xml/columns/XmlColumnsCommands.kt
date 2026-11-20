package net.unotec.app.commands.xml.columns

import net.unotec.app.commands.CommandContext
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters

@Command(mixinStandardHelpOptions = true, name = "columns", description = ["Colonnes XML"])
class XmlColumnsCommands(private val ctx: CommandContext) {
    @Command(mixinStandardHelpOptions = true, name = "load")
    fun load() {
        val columns = ctx.files.loadXmlFile(ctx.session.getXmlPath()).use { ctx.xml.loadColumns(it) }
        ctx.data.xmlColumns = columns
        ctx.resetMappings()
        show()
    }

    @Command(mixinStandardHelpOptions = true, name = "show")
    fun show() = println(ctx.data.xmlColumns.joinToString("\n").ifEmpty { "Aucune colonne XML." })

    @Command(mixinStandardHelpOptions = true, name = "add")
    fun add(@Parameters(paramLabel = "COLUMN") column: String) {
        require(ctx.files.loadXmlFile(ctx.session.getXmlPath()).use { ctx.xml.isColumnsExist(it, column) }) {
            "Colonne absente du fichier XML : $column"
        }
        ctx.data.xmlColumns = ctx.data.xmlColumns + column
    }

    @Command(mixinStandardHelpOptions = true, name = "remove")
    fun remove(@Parameters(paramLabel = "COLUMN") column: String) {
        require(column in ctx.data.xmlColumns) { "Colonne XML inconnue : $column" }
        ctx.data.xmlColumns = ctx.data.xmlColumns - column
        ctx.session.setMappedColumns(ctx.session.getMappedColumns().filter { it.xmlColumn != column })
        if (ctx.data.primaryKey == column) ctx.data.primaryKey = null
        ctx.invalidateRows()
    }
}
