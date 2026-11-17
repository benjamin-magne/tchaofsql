package net.unotec.app.services

import io.github.cdimascio.dotenv.dotenv
import net.unotec.app.config.PgConfiguration
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.PreparedStatement

class PgServiceTest {
    private val dotenv = dotenv {
        directory = ".."
        filename = ".env"
    }
   private val config = PgConfiguration(
        dotenv["HOST_TEST"],
        dotenv["PORT_TEST"].toInt(),
        dotenv["USER_TEST"],
        dotenv["MDP_TEST"],
        dotenv["DATABASE_TEST"],
        dotenv["SCHEMA_TEST"],
        dotenv["TABLE_TEST"])
    private val service = PgService(config)

    @Test
    fun should_connect_to_database_when_good_config() {
        //Arrange : déjà fait, variable config

        //Act
        val connection = service.connect()

        //Assert : test de connection
        connection.use { connection ->
            assertTrue(connection.isValid(5))
        }
    }

    @Test
    fun should_return_columns_metadata() {
        //Arrange : on récupère la connexion
        val connection = service.connect()

        //Act : récupération des infos des tables d'un schema
        val columnsMetaData = service.getColumnsData(connection)

        //Assert : 45 colonnes à la date du 16/11/2026
        assertEquals(45, columnsMetaData.size)
    }

    @Test
    fun return_one_column_metadata() {

        //Arrange : on récupère la connexion
        val connection = service.connect()

        //Act : récupération des infos des tables d'un schema
        val columnsMetaData = service.getColumnsData(connection, "lastname")

        //Assert : lastname est de type varchar à la date du 16/11/2026
        assertEquals(1, columnsMetaData.size)
        assertEquals("lastname",columnsMetaData[0].columnName)
    }

    @Test
    fun should_execute_rollback_statement() {
        //Arrange : il nous faut une connexion, une requête sql et un statement prêt et exécutable
        val connection = service.connect()

        val sql = """
    INSERT INTO public.llx_adherent
    (ref, entity, fk_adherent_type, morphy, lastname, firstname, email, statut)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
""".trimIndent()

        val statement: PreparedStatement = connection.prepareStatement(sql)

        statement.setString(1, "TEST-001")
        statement.setInt(2, 1)
        statement.setInt(3, 1)
        statement.setString(4, "phy")
        statement.setString(5, "Dupont")
        statement.setString(6, "Jean")
        statement.setString(7, "jean.dupont@example.com")
        statement.setInt(8, 1)

        statement.addBatch()

        //Act : true pour Dry-run -> rollback
        val result = service.executeBatch(connection, statement, true)

        //Assert, on doit avoir un ligne avec un 1
        assertEquals(1, result.size)
        assertEquals(1, result.first())

    }

}