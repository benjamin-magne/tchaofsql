package net.unotec.app.models

data class MappedPgColumnWithXmlValue(
    val pgColumnName: String,
    val value : String,
    val jdbcColumnType : Int
)
