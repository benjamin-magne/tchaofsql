package net.unotec.app.services

import io.mockk.every
import io.mockk.mockk
import net.unotec.app.models.DatabaseConfiguration
import net.unotec.app.models.MappedColumn
import net.unotec.app.models.PostgreColumnMetada
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Connection
import java.sql.SQLException
import java.sql.Types
import kotlin.test.assertEquals

class SqlWriterServiceTest {

    private val connection = mockk<Connection>()
    private val configuration = mockk<DatabaseConfiguration>()

    private val service = SqlWriterService(connection, configuration)



    @Test
    fun should_return_three_interrogation() {
        //Arrange : On créer une liste de 3 colonnes mappées
        val mappedColumns = listOf(
            MappedColumn(
                "IDPERSONNE",
                PostgreColumnMetada("id",4, "integer",Types.INTEGER,"NO"),
                ),
            MappedColumn(
                "NOM",
                PostgreColumnMetada("name",255, "varchar",Types.VARCHAR,"YES"),
            ),
            MappedColumn(
                "AGE",
                PostgreColumnMetada("age",4, "integer",Types.INTEGER,"YES"),
            )
        )
        //Act
        val result = service.mappedColumnsToValues(mappedColumns)

        //Assert
        assertEquals("?, ?, ?", result)
    }

    @Test
    fun should_return_three_parameters() {
        //Arrange : On créer une liste de 3 colonnes mappées
        val mappedColumns = listOf(
            MappedColumn(
                "IDPERSONNE",
                PostgreColumnMetada("id",4, "integer",Types.INTEGER,"NO"),
            ),
            MappedColumn(
                "NOM",
                PostgreColumnMetada("name",255, "varchar",Types.VARCHAR,"YES"),
            ),
            MappedColumn(
                "AGE",
                PostgreColumnMetada("age",4, "integer",Types.INTEGER,"YES"),
            )
        )
        //Act
        val result = service.mappedColumnsToParameters(mappedColumns)

        //Assert
        assertEquals(""""id", "name", "age"""", result)
    }

    @Test
    fun should_return_two_updates_parameters() {
        //Arrange : On créer une liste de 3 colonnes mappées et une clé primaire
        val mappedColumns = listOf(
            MappedColumn(
                "IDPERSONNE",
                PostgreColumnMetada("id",4, "integer",Types.INTEGER,"NO"),
            ),
            MappedColumn(
                "NOM",
                PostgreColumnMetada("name",255, "varchar",Types.VARCHAR,"YES"),
            ),
            MappedColumn(
                "AGE",
                PostgreColumnMetada("age",4, "integer",Types.INTEGER,"YES"),
            )
        )
        val pkColumn = "id"
        //Act
        val result = service.mappedColumnsToUpdates(mappedColumns, pkColumn)

        //Assert
        assertEquals("""ON CONFLICT ("id") DO UPDATE SET "name" = EXCLUDED."name", "age" = EXCLUDED."age"""", result)
    }

    @Test
    fun should_return_correct_base_sql_script() {
        //Arrange : On créer une liste de 3 colonnes mappées et une clé primaire
        val mappedColumns = listOf(
            MappedColumn(
                "IDPERSONNE",
                PostgreColumnMetada("id",4, "integer",Types.INTEGER,"NO"),
            ),
            MappedColumn(
                "NOM",
                PostgreColumnMetada("name",255, "varchar",Types.VARCHAR,"YES"),
            ),
            MappedColumn(
                "AGE",
                PostgreColumnMetada("age",4, "integer",Types.INTEGER,"YES"),
            )
        )
        val pkColumn = "id"

        every { configuration.schema } returns "public"
        every { configuration.table } returns "table"
        //Act
        val result = service.baseSqlScript(mappedColumns, pkColumn)
        //Assert
        val expectedResult = """
            INSERT INTO "public"."table" 
            ("id", "name", "age")
            VALUES (?, ?, ?)
            ON CONFLICT ("id") DO UPDATE SET "name" = EXCLUDED."name", "age" = EXCLUDED."age
        """.trimIndent()
    }
}