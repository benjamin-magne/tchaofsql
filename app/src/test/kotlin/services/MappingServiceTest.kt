package net.unotec.app.services

import net.unotec.app.models.PostgreColumn
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
        val pgColumns = listOf<PostgreColumn>(
            PostgreColumn("id","INTEGER", Types.INTEGER,4,"NO"),
            PostgreColumn("name","VARCHAR", Types.VARCHAR,255,"YES")
        )

        //Act : on appelle la méthode
        service.add(xmlColumns.first(), pgColumns[0].columnName)

        //Assert : on vérifie que le mapping est bon
        assertEquals(mapOf("ID_PERSONNE" to "id"), map)
    }

    @Test
    fun should_remove_all_mapped_columns() {
        //Assert : on ajoute plusieurs map à note varaible privé
        map.putIfAbsent("ID_PERSONNE", "id")
        map.putIfAbsent("NOM", "name")

        //Act
        service.removeAll()

        //Assert
        assertEquals(0, map.size)

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
        val pgColumn = PostgreColumn("id","INTEGER", Types.INTEGER,4,"YES")

        //Act
        val isColumnMapped = service.isNullableColumnMapped(pgColumn)

        //Assert
        assertEquals(false, isColumnMapped)

    }

}