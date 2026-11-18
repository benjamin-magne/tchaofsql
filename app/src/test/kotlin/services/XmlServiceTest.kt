package net.unotec.app.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader
import kotlin.io.path.writeText

class XmlServiceTest {

    val xmlService = XmlService()

    @Test
    fun should_return_columns_list() {
        //Arrange on utilise les données du data.xml
        val url = requireNotNull(javaClass.getResource("/data.xml"))
        val file = File(url.toURI())


        file.inputStream().use { input ->
            val factory = XMLInputFactory.newInstance()
            val reader = factory.createXMLStreamReader(input)

            //Act : on appelle la fonction
            val columns = xmlService.listColumns(reader)

            //Assert
            assertEquals(2, columns.size)
        }

    }

    @Test
    fun should_return_true_when_search_existing_column() {
        //Arrange on utilise les données du data.xml
        val url = requireNotNull(javaClass.getResource("/data.xml"))
        val file = File(url.toURI())

        file.inputStream().use { input ->
            val factory = XMLInputFactory.newInstance()
            val reader = factory.createXMLStreamReader(input)

            //Act : on appelle la fonction
            val columnExist = xmlService.isColumnExist(reader, "NOM")

            //Assert
            assertEquals(true, columnExist)
        }
    }

    @Test
    fun should_return_two_and_martin_when_get_data() {
        //Arrange on utilise les données du data.xml
        val url = requireNotNull(javaClass.getResource("/data.xml"))
        val file = File(url.toURI())

        file.inputStream().use { input ->
            val factory = XMLInputFactory.newInstance()
            val reader = factory.createXMLStreamReader(input)

            //Act : on appelle la fonction
            val dataList = xmlService.loadRecords(reader)

            //Assert
            assertEquals("2", dataList[1]["IDPERSONNE"])
            assertEquals("Martin", dataList[1]["NOM"])
        }
    }

}