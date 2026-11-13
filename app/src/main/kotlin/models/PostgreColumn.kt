package net.unotec.app.models

data class PostgreColumn(
    val columnName: String,
    val columnType: String,
    val columnSize: Int,
    val columnIsNullable: String
)
