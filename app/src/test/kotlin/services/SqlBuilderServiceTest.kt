package net.unotec.app.services

import io.github.cdimascio.dotenv.dotenv
import net.unotec.app.config.PgConfiguration
import net.unotec.app.models.PgColumn
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Types
import kotlin.text.toInt

class SqlBuilderServiceTest {

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
        "public",
        "test",
    )
    private val service = SqlBuilderService(config)

    @Test
    fun should_return_two_columns_and_two_values_script() {
        //Assert : on créer une liste de map

        val mappedColumns = mapOf<String, String>(
            "ID_PERSONNE" to "id",
            "NOM" to "name"
        )

        //Act
        val sql = service.buildInsertSql(mappedColumns)

        //Assert
        val expectedResult = """
            INSERT INTO "public"."test"
            ("id", "name")
            VALUES (?, ?)
        """.trimIndent()
        assertEquals(expectedResult, sql)
    }

    @Test
    fun should_return_two_columns_two_values_script_with_() {
        //Assert : on créer une liste de map

        val mappedColumns = mapOf<String, String>(
            "ID_PERSONNE" to "id",
            "NOM" to "name",
            "AGE" to "age"
        )

        //Act
        val sql = service.buildUpsertSql(mappedColumns, "id")

        //Assert
        val expectedResult = """
            INSERT INTO "public"."test"
            ("id", "name", "age")
            VALUES (?, ?, ?) ON CONFLICT ("id") DO UPDATE SET "name" = EXCLUDED."name", "age" = EXCLUDED."age"
        """.trimIndent()
        assertEquals(expectedResult, sql)
    }
}