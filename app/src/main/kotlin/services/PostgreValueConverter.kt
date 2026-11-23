package net.unotec.app.services

import net.unotec.app.models.PostgreColumnMetada
import java.sql.Types
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle
import java.util.Locale

/** Normalizes WinDev DateHeure values without changing text columns or adding a time zone. */
class PostgreValueConverter {
    private val windevDateTime = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss", Locale.ROOT)
        .withResolverStyle(ResolverStyle.STRICT)
    private val postgreDateTime = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss", Locale.ROOT)

    fun convert(value: String?, column: PostgreColumnMetada): String? {
        if (value == null || column.jdbcType !in listOf(Types.TIMESTAMP, Types.TIMESTAMP_WITH_TIMEZONE)) return value
        // Preserve inputs already supported by JDBC, including ISO timestamps and empty values.
        if ('/' !in value) return value

        return try {
            LocalDateTime.parse(value.trim(), windevDateTime).format(postgreDateTime)
        } catch (error: DateTimeParseException) {
            throw IllegalArgumentException(
                "DateHeure WinDev invalide pour la colonne '${column.name}' : '$value'. " +
                    "Format attendu : jj/MM/aaaa HH:mm:ss.",
                error
            )
        }
    }
}
