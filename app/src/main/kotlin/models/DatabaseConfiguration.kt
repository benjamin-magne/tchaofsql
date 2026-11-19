package net.unotec.app.models

data class DatabaseConfiguration(
    var host: String,
    var port: Int,
    var username: String,
    var password: String,
    var database: String,
    var schema: String,
    var table: String
)
