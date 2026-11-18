package net.unotec.app.services

import io.mockk.every
import io.mockk.mockk
import net.unotec.app.models.DatabaseConfiguration
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Connection


class DatabaseServiceTest {

    val service = mockk<DatabaseService>()

    @Test
    fun should_return_working_connection_value_when_configuration_is_good() {
        //Arrange : création d'un config simulé
        val config = mockk<DatabaseConfiguration>()
        val connection = mockk<Connection>()
        //Act

        every { service.connect(config) } returns connection
        val result = service.connect(config)

        //Assert
        every { result.isValid(5) } returns true
        assertTrue { result.isValid(5) }
    }

    @Test
    fun should_return_true_when_connection_is_ok() {
        //Arrange : création d'un config simulé
        val config = mockk<DatabaseConfiguration>()

        //Act
        every { service.testConnect(config) } returns true

        //Assert
        assertTrue { service.testConnect(config) }
    }

    @Test
    fun should_return_missing_configuration_properties() {
        //Arrange : création d'un config simulé
        val config = mockk<DatabaseConfiguration>()

        //Act
        every { service.checkConfiguration(config) } throws IllegalArgumentException("L'hôte n'est pas joignable")

        //Assert
        assertThrows<IllegalArgumentException> { service.checkConfiguration(config) }
    }

}