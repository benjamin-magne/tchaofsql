package net.unotec.app.commands.xml.file

import net.unotec.app.commands.CommandContext
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.io.File

@Command(mixinStandardHelpOptions = true, name = "file", description = ["Fichier XML de la session"])
class XmlFileCommand(private val ctx: CommandContext) {
    @Command(mixinStandardHelpOptions = true, name = "load")
    fun load(@Parameters(paramLabel = "FILE") path: String) {
        ctx.files.loadXmlFile(path).use { ctx.xml.loadColumns(it) }
        ctx.session.setXmlPath(File(path).absolutePath)
        ctx.data.xmlColumns = emptySet()
        ctx.data.xmlRecords = emptyList()
        ctx.resetMappings()
        println("Fichier chargÃ© : ${ctx.session.getXmlPath()}")
    }

    @Command(mixinStandardHelpOptions = true, name = "show")
    fun show() = println(ctx.session.getXmlPath().ifEmpty { "Aucun fichier chargÃ©." })
}


