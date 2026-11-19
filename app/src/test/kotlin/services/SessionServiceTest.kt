package net.unotec.app.services

import net.unotec.app.models.PostgreColumnMetada
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Types

class SessionServiceTest {

    private val sessionService = SessionService()

    @Test
    fun should_set_mapped_rows_and_return_readable_rows() {
        //Assert, on créer les enregistrements mappés
        val mappedRows = listOf(
            mapOf(
                PostgreColumnMetada("id",4, "integer",Types.INTEGER,"NO") to "1",
                PostgreColumnMetada("name",255, "varchar",Types.VARCHAR,"YES") to "Martin",
            ),
            mapOf(
                PostgreColumnMetada("id",4, "integer",Types.INTEGER,"NO") to "2",
                PostgreColumnMetada("name",255, "varchar",Types.VARCHAR,"YES") to "Benjoss",
            )
        )

        //Act
        sessionService.setMappedRows(mappedRows)

        //Assert
        println(sessionService.getMappedRows())
        assertEquals(2, sessionService.getMappedRows().size)

    }

}