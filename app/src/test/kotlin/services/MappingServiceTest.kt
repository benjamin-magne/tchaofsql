package net.unotec.app.services

import net.unotec.app.models.PostgreColumn
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Types

class MappingServiceTest {

    private val service = MappingService()

    @Test
    fun should_map_columns() {
        //Assert on créer une liste de colonnes XML et PostgreSQL
        val xmlColumns = setOf<String>("ID_PERSONNE", "NOM")
        val pgColumns = listOf<PostgreColumn>(
            PostgreColumn("id","INTEGER", Types.INTEGER,4,"NO"),
            PostgreColumn("name","VARCHAR", Types.VARCHAR,255,"YES")
        )

        //Act : on appelle la méthode


        //Assert : on vérifie que le mapping est bon
    }

}