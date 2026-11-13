package net.unotec.app.config

data class PostgreConfig(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val database: String,
    val schema: String,
    val table: String
)
