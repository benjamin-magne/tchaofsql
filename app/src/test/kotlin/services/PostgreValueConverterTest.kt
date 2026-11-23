package net.unotec.app.services

import net.unotec.app.models.PostgreColumnMetada
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Types
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PostgreValueConverterTest {
    private val converter = PostgreValueConverter()
    private val timestamp = PostgreColumnMetada("created_at", 0, "timestamp", Types.TIMESTAMP, "YES")

    @Test
    fun `converts WinDev day month year without swapping day and month`() {
        assertEquals("2026-08-01 11:50:00", converter.convert("01/08/2026 11:50:00", timestamp))
        assertEquals("2026-12-25 00:00:00", converter.convert("25/12/2026 00:00:00", timestamp))
        assertEquals("2024-02-29 23:59:59", converter.convert("29/02/2024 23:59:59", timestamp))
    }

    @Test
    fun `rejects invalid dates times and incomplete WinDev timestamps`() {
        for (value in listOf("31/02/2026 11:50:00", "29/02/2026 11:50:00", "01/08/2026 24:00:00",
            "01/08/2026 11:60:00", "01/08/2026")) {
            val error = assertThrows<IllegalArgumentException> { converter.convert(value, timestamp) }
            assertTrue(error.message!!.contains("created_at"))
            assertTrue(error.message!!.contains("jj/MM/aaaa HH:mm:ss"))
        }
    }

    @Test
    fun `preserves values already supported by JDBC and nulls`() {
        for (value in listOf(null, "", "2026-08-01 11:50:00", "2026-08-01T11:50:00.123")) {
            assertEquals(value, converter.convert(value, timestamp))
        }
    }

    @Test
    fun `does not convert DateHeure strings in non timestamp columns`() {
        for (type in listOf(Types.VARCHAR, Types.DATE, Types.TIME, Types.INTEGER)) {
            assertEquals("01/08/2026 11:50:00",
                converter.convert("01/08/2026 11:50:00", timestamp.copy(jdbcType = type)))
        }
    }

    @Test
    fun `normalizes timestamp with timezone without inventing an offset`() {
        assertEquals("2026-08-01 11:50:00", converter.convert("01/08/2026 11:50:00",
            timestamp.copy(postgreType = "timestamptz", jdbcType = Types.TIMESTAMP_WITH_TIMEZONE)))
    }
}
