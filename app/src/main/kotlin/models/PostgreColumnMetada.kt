package net.unotec.app.models

data class PostgreColumnMetada(
    val name : String,
    val size : Int,
    val postgreType : String,
    val jdbcType : Int,
    val isNullable : String
)
