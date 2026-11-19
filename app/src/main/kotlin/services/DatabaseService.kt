package net.unotec.app.services

import net.unotec.app.interfaces.IDatabaseService
import net.unotec.app.models.DatabaseConfiguration
import java.net.InetAddress
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseService : IDatabaseService {
    override fun connect(configuration: DatabaseConfiguration): Connection {
        val url = "jdbc:postgresql://${configuration.host}:${configuration.port}/${configuration.database}"
        return DriverManager.getConnection(url, configuration.username, configuration.password)
    }

    override fun testConnect(configuration: DatabaseConfiguration): Boolean { return connect(configuration).isValid(5) }

    override fun checkConfiguration(configuration: DatabaseConfiguration): Boolean {
        require(configuration.host.isNotBlank()) { "Aucun hôte renseigné" }
        require(configuration.username.isNotBlank()) { "Aucun utilisateur renseigné" }
        require(configuration.password.isNotBlank()) { "Aucun mot de passe renseigné" }
        require(configuration.database.isNotBlank()) { "Aucune base de donnée renseigné" }
        require(configuration.schema.isNotBlank()) { "Aucun schéma renseigné" }
        require(configuration.table.isNotBlank()) { "Aucune table renseignée" }
        return true
    }
}