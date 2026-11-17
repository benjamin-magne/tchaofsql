package net.unotec.app.models

data class PgColumn(
    val columnName: String,
    val jdbcColumnType: Int,
    val columnIsNullable: String
)
