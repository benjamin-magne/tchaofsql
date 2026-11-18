package net.unotec.app.services

import net.unotec.app.interfaces.IXmlReaderService
import java.io.FileInputStream
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamReader

class XmlReaderService() : IXmlReaderService {

    private val invalidColumnsList = listOf<String>("WINDEV_TABLE", "TABLE_CONTENU","N__Enr")

    override fun loadColumns(fileInputStream: FileInputStream): Set<String> {
        val columns = mutableSetOf<String>()
        val xml = createXmlStreamReader(fileInputStream)
        while (xml.hasNext()) { if (xml.next() == XMLStreamConstants.START_ELEMENT && !isInvalidColumn(xml.localName)) { columns.add(xml.localName) } }
        return columns
    }

    override fun loadRecords(fileInputStream: FileInputStream): List<Map<String, String>> {
        val records = mutableListOf<Map<String, String>>()
        var record = mutableMapOf<String, String>()

        val xml = createXmlStreamReader(fileInputStream)
        while (xml.hasNext()) {
            if (xml.next() == XMLStreamConstants.START_ELEMENT) {
                while (!isInvalidColumn(xml.localName)) {
                    record[xml.localName] = xml.elementText
                    xml.nextTag()
                }
                if (record.isNotEmpty()) {
                    records.add(record)
                    record = mutableMapOf<String, String>()
                }
            }
        }
        return records
    }

    override fun isColumnsExist(fileInputStream: FileInputStream, column : String): Boolean {
        return loadColumns(fileInputStream).contains(column)
    }

    private fun createXmlStreamReader(fileInputStream: FileInputStream): XMLStreamReader {
        val factory = XMLInputFactory.newInstance().apply {
            setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false)
            setProperty(XMLInputFactory.SUPPORT_DTD, false)
        }
        return factory.createXMLStreamReader(fileInputStream)
    }

    private fun isInvalidColumn(name: String): Boolean { return invalidColumnsList.contains(name) }
}