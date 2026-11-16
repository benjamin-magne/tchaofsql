package net.unotec.app.models

data class PostgreColumn(
    val columnName: String,
    val columnType: String,
    val jdbcColumnType: Int,
    val columnSize: Int,
    val columnIsNullable: String
)
