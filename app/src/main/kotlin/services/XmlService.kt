package net.unotec.app.services

import java.io.File
import java.io.InputStream
import javax.xml.stream.Location
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamReader

class XmlService {

    // Responsabilité des exports XML
    private val invalidColumnNames = listOf("WINDEV_TABLE", "TABLE_CONTENU","N__Enr")

    fun listColumns(reader: XMLStreamReader) : Set<String> {
        val columns = mutableSetOf<String>()
        while (reader.hasNext()) {
            when (reader.next()) { XMLStreamConstants.START_ELEMENT -> { if (isColumnValid(reader)) { columns.add(reader.localName) } } }
        }
        return columns
    }

    fun isColumnExist(reader: XMLStreamReader, columnName: String) : Boolean { return listColumns(reader).contains(columnName) }

    fun loadRecords(reader: XMLStreamReader) : List<Map<String, String>> {
        val records = mutableListOf<Map<String, String>>()
        var record = mutableMapOf<String, String>()

        while (reader.hasNext()) {
            when (reader.next()) {
                XMLStreamConstants.START_ELEMENT -> {
                    while (isColumnValid(reader)) {
                        record[reader.localName] = reader.elementText
                        reader.nextTag()
                    }
                    if (record.isNotEmpty()) {
                        records.add(record)
                        record = mutableMapOf()
                    }
                }
            }
        }
        return records
    }

    private fun isColumnValid(reader: XMLStreamReader) : Boolean { return reader.localName !in invalidColumnNames }
}