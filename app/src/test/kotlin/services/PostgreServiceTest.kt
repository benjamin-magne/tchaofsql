package net.unotec.app.services

import io.github.cdimascio.dotenv.dotenv
import net.unotec.app.config.PostgreConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PostgreServiceTest {
    private val dotenv = dotenv {
        directory = ".."
        filename = ".env"
    }
   private val config = PostgreConfig(
        dotenv["HOST_TEST"],
        dotenv["PORT_TEST"].toInt(),
        dotenv["USER_TEST"],
        dotenv["MDP_TEST"],
        dotenv["DATABASE_TEST"],
        dotenv["SCHEMA_TEST"],
        dotenv["TABLE_TEST"])
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

    @Test
    fun return_one_column_metadata() {

    }

}