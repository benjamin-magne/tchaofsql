package net.unotec.app.services

import java.sql.Types

class TypeService {

    fun convert(value: String, dataType: Int) : Any? {
        return when(dataType) {
            Types.SMALLINT -> value.toShortOrNull()
            Types.INTEGER -> value.toIntOrNull()
            Types.BIGINT -> value.toLongOrNull()
            Types.NUMERIC -> value.toBigDecimalOrNull()
            Types.REAL -> value.toFloatOrNull()
            Types.DOUBLE -> value.toDoubleOrNull()
            Types.CHAR -> value
            Types.VARCHAR -> value
            Types.BIT -> value.toBoolean()
            Types.BINARY -> value.toByteArray()
            else -> throw IllegalArgumentException("Type non supporté : $dataType")
        }
    }
}