package net.unotec.app.services

import io.github.cdimascio.dotenv.dotenv
import net.unotec.app.config.PostgreConfig
import net.unotec.app.models.PostgreColumn
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Types
import kotlin.text.toInt

class SqlBuilderServiceTest {

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
        "public",
        "test",
    )
    private val service = SqlBuilderService(config)

    @Test
    fun should_return_two_columns_and_two_values_script() {
        //Assert : on créer une liste de pgcolumn avec une liste de map
        val pgColumns = listOf<PostgreColumn>(
            PostgreColumn("id","integer", Types.INTEGER,4,"NO"),
            PostgreColumn("name","varchar", Types.VARCHAR,255,"YES"),
        )

        val mappedColumns = mapOf<String, String>(
            "ID_PERSONNE" to "id",
            "NOM" to "name"
        )

        //Act
        val sql = service.buildBaseScript(mappedColumns, pgColumns)

        //Assert
        val expectedResult = """
            INSERT INTO public.test (id,name)
            VALUES (?,?)
        """.trimIndent()
        assertEquals(expectedResult, sql)
    }

}