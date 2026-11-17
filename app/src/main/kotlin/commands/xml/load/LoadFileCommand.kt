package net.unotec.app.commands.xml.load

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import net.unotec.app.data.XmlServiceData
import net.unotec.app.services.XmlService
import java.io.IOException
import java.io.InputStream
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader
import kotlin.io.path.inputStream

class LoadFileCommand(private val data : XmlServiceData) : CliktCommand("file") {

    private val file by argument().path(mustExist = true, canBeDir = false, mustBeReadable = true)

    override fun run() { try { data.xmlStreamReader = createXmlReader(file.inputStream()) } catch(ex: XMLStreamException) {
        echo("Error while reading file ${ex.message}")
        throw ex
    } }

    private fun createXmlReader(input : InputStream): XMLStreamReader {
        val factory = XMLInputFactory.newFactory().apply {
            setProperty(XMLInputFactory.SUPPORT_DTD, false)
            setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false)
        }
        return factory.createXMLStreamReader(input)
    }
}