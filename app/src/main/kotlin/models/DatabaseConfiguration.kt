package net.unotec.app.models

data class DatabaseConfiguration(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val database: String,
    val schema: String,
    val table: String
)
