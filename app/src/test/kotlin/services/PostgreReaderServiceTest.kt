package net.unotec.app.services

import io.mockk.every
import io.mockk.mockk
import net.unotec.app.models.DatabaseConfiguration
import net.unotec.app.models.PostgreColumnMetada
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.Types

class PostgreReaderServiceTest {

    private val service = mockk<PostgreReaderService>()

    @Test
    fun should_return_list_of_two_postgrecolumnmetadata() {
        //Arrange : on va créer des colonnes , une connecxion fictive et une configuration fictive
        val columnsList = listOf<PostgreColumnMetada>(
            PostgreColumnMetada("firstname",255, "varchar",Types.VARCHAR,"YES"),
            PostgreColumnMetada("lastname",255, "varchar",Types.VARCHAR,"YES"),
        )

        val connection = mockk<Connection>()
        val configuration = mockk<DatabaseConfiguration>()

        //Assert
        every { service.getColumnsMetadata(connection, configuration) } returns columnsList
        val result = service.getColumnsMetadata(connection, configuration)

        //Act
        assertEquals(2, result.size)
        assertEquals(Types.VARCHAR, result.first().jdbcType)
    }

    @Test
    fun should_return_one_postgrecolumnmetadata() {
        //Arrange : on va créer des colonnes , une connexion fictive, configuration fictive et une colonne de recherche fictive
        val columnsList = listOf<PostgreColumnMetada>(
            PostgreColumnMetada("firstname",255, "varchar",Types.VARCHAR,"YES"),
            PostgreColumnMetada("lastname",255, "varchar",Types.VARCHAR,"YES"),
        )

        val connection = mockk<Connection>()
        val configuration = mockk<DatabaseConfiguration>()

        val columnName = "firstname"

        //Assert
        every { service.getColumnsMetadata(connection, configuration, columnName) } returns listOf(columnsList.first())
        val result = service.getColumnsMetadata(connection, configuration, columnName)

        //Act
        assertEquals(1, result.size)
        assertEquals(columnName, result.first().name)
    }

}