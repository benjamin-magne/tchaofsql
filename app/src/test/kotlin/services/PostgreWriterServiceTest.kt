package net.unotec.app.services

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class PostgreWriterServiceTest {

    private val service = mockk<PostgreWriterService>()

    @Test
    fun should_return_one_when_execution_is_successful() {
        //Assert : on créer, une connexion factice ainsi qu'un statement factice (la statement contient une requête UPSERT)
        val connection = mockk<Connection>()
        val preparedStatement = mockk<PreparedStatement>()

        //Act
        every { service.executeUpsertStatement(connection, preparedStatement, false) } returns 1
        val result = service.executeUpsertStatement(connection, preparedStatement, dryRun = false)

        //Assert
        assertEquals(1, result)
    }

    @Test
    fun should_return_exception_when_execution_is_failure() {
        //Assert : on créer, une connexion factice ainsi qu'un statement factice (la statement contient une requête UPSERT)
        val badConnection = mockk<Connection>()
        val preparedStatement = mockk<PreparedStatement>()

        //Act
        every { service.executeUpsertStatement(badConnection, preparedStatement) } throws SQLException()

        //Assert
        assertThrows<SQLException>() { service.executeUpsertStatement(badConnection, preparedStatement) }

    }
}