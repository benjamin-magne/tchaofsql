package net.unotec.app.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path

class FileLoaderServiceTest {

    private val service = FileLoaderService()

    @Test
    fun should_return_fileinputstream() {
        //Arrange : on va chercher le fichier test
        val filePath: String = requireNotNull(
            javaClass.getResource("/data.xml")
        ).toURI().let { Path.of(it).toString() }

        //Act
        val inputStream = service.loadXmlFile(filePath)

        //Assert : On vérifie que le fichier est lisible
        inputStream.use { assertTrue { it.read() != -1 }}
    }
}