package net.unotec.app.services

import net.unotec.app.models.PgColumn
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Types

class MappingServiceTest {


    private val map = mutableMapOf<String, String>()
    private val service = MappingService(map)

    @Test
    fun should_map_columns() {
        //Assert on créer une liste de colonnes XML et PostgreSQL
        val xmlColumns = setOf<String>("ID_PERSONNE", "NOM")
        val pgColumns = listOf<PgColumn>(
            PgColumn("id", Types.INTEGER,"NO"),
            PgColumn("name",Types.VARCHAR,"YES")
        )

        //Act : on appelle la méthode
        service.add(xmlColumns.first(), pgColumns[0].columnName)

        //Assert : on vérifie que le mapping est bon
        assertEquals(mapOf("ID_PERSONNE" to "id"), map)
    }

    @Test
    fun should_remove_one_mapped_column() {
        map.putIfAbsent("ID_PERSONNE", "id")
        map.putIfAbsent("NOM", "name")
        //Act
        service.removeMappedColumn("NOM")

        //Assert
        assertEquals(1, map.size)
    }

    @Test
    fun should_change_one_mapped_column() {
        map.putIfAbsent("NOM", "name")
        //Act
        service.change("NOM", "nom")

        //Assert
        assertEquals(mapOf("NOM" to "nom"), map)
    }

    @Test
    fun should_return_false_when_nullable_column_is_not_mapped() {
        //Assert : on créer une colonne nullable et on ne l'affecte pas au map
        val pgColumn = PgColumn("id", Types.INTEGER,"YES")

        //Act
        val isColumnMapped = service.isNullableColumnMapped(pgColumn)

        //Assert
        assertEquals(false, isColumnMapped)

    }

    @Test
    fun should_return_mappedpgcolumnwithxmlvalue_list() {
        //Assert : on créer une liste d'enregistrement XML, liste de colonne pg et on complète la variable privé map
        val xmlRecords = listOf(mapOf("ID_PERSONNE" to "1", "NOM" to "Benjouss"))

        val pgColumnList = listOf(
            PgColumn("id",Types.INTEGER,"NO"),
            PgColumn("name",Types.VARCHAR,"YES")
        )

        map["ID_PERSONNE"] = "id"
        map["NOM"] = "name"

        //Act : on appelle ma méthode
        val result = service.mapXmlValuesWithPgColumns(xmlRecords, pgColumnList)

        //Assert on vérifie l'ensemble de nos résultats
        assertEquals("id",result[0].pgColumnName)
        assertEquals("1",result[0].value)
        assertEquals("name",result[1].pgColumnName)
        assertEquals("Benjouss",result[1].value)
    }

}