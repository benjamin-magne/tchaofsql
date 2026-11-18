package net.unotec.app.services

import net.unotec.app.models.MappedColumn
import net.unotec.app.models.PostgreColumnMetada
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Types

class MapperServiceTest {

    private val service = MapperService()

    @Test
    fun should_return_mapped_column_object() {
        //Arrange : on créer une colonne XML et une colonne Postgre
        val xmlColumn = "ID_PERSONNE"
        val postgreColumn = PostgreColumnMetada("id",4, "integer",Types.INTEGER,"NO")

        //Act
        val result = service.mapColumn(xmlColumn, postgreColumn)

        //Assert
        assertEquals("ID_PERSONNE", result.xmlColumn)
        assertEquals("id", result.postgreColumn.name)
    }

    @Test
    fun should_return_mapped_row() {
        //Arrange : on créer un enregistrement XML (colonne + valeur) et une liste de colonne mappé
        val xmlRow = mapOf("ID_PERSONNE" to "1", "NOM" to "Martin")
        val mappedColumns = listOf(
            MappedColumn(
                "ID_PERSONNE",
                PostgreColumnMetada("id",4, "integer",Types.INTEGER,"NO")
            ),
            MappedColumn(
                "NOM",
                PostgreColumnMetada("name",255, "varchar",Types.VARCHAR,"YES")
            )
        )

        //Act
        val result = service.mapRows(xmlRow, mappedColumns)

        //Assert
        val expected = mapOf(
            mappedColumns.first().postgreColumn to xmlRow.entries.first().value,
            mappedColumns.last().postgreColumn to xmlRow.entries.last().value
        )
        assertEquals(expected, result)
    }

    @Test
    fun should_return_new_primary_key() {
        //Arrange : on créer une colonne XML et une liste de colonne mappé
        val xmlColumn = "ID_PERSONNE"
        val mappedColumns = listOf(
            MappedColumn(
                "ID_PERSONNE",
                PostgreColumnMetada("id",4, "integer",Types.INTEGER,"NO")
            ),
            MappedColumn(
                "NOM",
                PostgreColumnMetada("name",255, "varchar",Types.VARCHAR,"YES")
            )
        )

        //Act
        val result = service.setPkColumn(xmlColumn, mappedColumns)

        //Assert
        assertEquals("ID_PERSONNE", result)
    }

}