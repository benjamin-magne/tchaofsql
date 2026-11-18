package net.unotec.app.models

data class MappedColumn(
    val xmlColumn : String,
    val postgreColumn : PostgreColumnMetada,
)