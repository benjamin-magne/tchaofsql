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
/*
    fun getPostgreType(dataType: Int): String {
        return when (dataType) {
            Types.SMALLINT -> "SMALLINT"
            Types.INTEGER -> "INTEGER"
            Types.BIGINT -> "BIGINT"
            Types.REAL -> "REAL"
            Types.FLOAT, Types.DOUBLE -> "DOUBLE PRECISION"
            Types.NUMERIC, Types.DECIMAL -> "NUMERIC"

            Types.CHAR -> "CHAR"
            Types.VARCHAR -> "VARCHAR"
            Types.LONGVARCHAR -> "TEXT"

            Types.BOOLEAN, Types.BIT -> "BOOLEAN"

            Types.DATE -> "DATE"
            Types.TIME -> "TIME"
            Types.TIMESTAMP -> "TIMESTAMP"
            Types.TIME_WITH_TIMEZONE -> "TIME WITH TIME ZONE"
            Types.TIMESTAMP_WITH_TIMEZONE -> "TIMESTAMP WITH TIME ZONE"

            Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY -> "BYTEA"
            Types.BLOB -> "BYTEA"

            Types.ARRAY -> "ARRAY"
            Types.SQLXML -> "XML"

            Types.OTHER -> "OTHER"

            else -> throw IllegalArgumentException(
                "Type JDBC non pris en charge : $dataType"
            )
        }
    }*/
}