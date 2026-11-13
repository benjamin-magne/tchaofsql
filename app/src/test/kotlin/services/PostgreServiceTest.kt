package net.unotec.app.services

import net.unotec.app.config.PostgreConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class PostgreServiceTest {

    private val config = PostgreConfig(
        "192.168.102.4",
        5432,
        "dolibarr_app",
        "ADMUN0TEC",
        "dolibarr24",
        "public",
        "llx_adherent")
    private val service = PostgreService(config)

    @Test
    fun should_connect_to_database_when_good_config() {
        //Assert : déjà fait, variable config

        //Act
        val connection = service.connect()

        //Assert : test de connection
        connection.use { connection ->
            assertTrue(connection.isValid(5))
        }
    }

    @Test
    fun should_return_columns_metadata() {
        //Assert : on récupère la connexion
        val connection = service.connect()

        //Act
        val columnsMetaData = service.getColumnsData(connection)

        //Assert
        for (column in columnsMetaData) {
            println(column.columnName+" "+column.columnType+" "+column.columnSize+" "+column.columnIsNullable)
        }
    }

}