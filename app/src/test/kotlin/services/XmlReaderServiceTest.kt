package net.unotec.app.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.FileInputStream
import java.nio.file.Path

class XmlReaderServiceTest {
    private val service = XmlReaderService()

    private val filePath: String = requireNotNull(
        javaClass.getResource("/data.xml")
    ).toURI().let { Path.of(it).toString() }

    @Test
    fun should_return_set_of_2_columns() {
        //Arrange : on récupère le input stream
        val fileInputStream = FileInputStream(filePath)

        //Act
        val result = service.loadColumns(fileInputStream)

        //Assert
        assertEquals(2, result.size)
        assertEquals("IDPERSONNE", result.first())
    }

    @Test
    fun should_return_two_list_of_two_map() {
        //Arrange : on récupère le input stream
        val fileInputStream = FileInputStream(filePath)

        //Act
        val result = service.loadRecords(fileInputStream)

        assertEquals(3, result.size)
        assertEquals(2, result.first().size)
        assertEquals(mapOf("IDPERSONNE" to "1", "NOM" to "Dupont"), result.first())
    }

    @Test
    fun should_return_column_does_not_exist() {
        //Arrange : on récupère le input stream
        val fileInputStream = FileInputStream(filePath)

        //Act
        val result = service.isColumnsExist(fileInputStream, "EXISTE_PAS")

        //Assert
        assertFalse { result }
    }

}