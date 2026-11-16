package net.unotec.app.services

import net.unotec.app.config.PostgreConfig
import net.unotec.app.models.PostgreColumn
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class PostgreService(private val config: PostgreConfig) {

    fun connect() : Connection{
        val url = "jdbc:postgresql://${config.host}:${config.port}/${config.database}"

        return DriverManager.getConnection(
            url,
            config.username,
            config.password
        )
    }

    fun getColumnsData(connection: Connection, columnName : String? = null) : List<PostgreColumn> {
        val columnsList = mutableListOf<PostgreColumn>()
        connection.metaData.getColumns(null, config.schema, config.table, columnName).use { columnsData ->
            while (columnsData.next()) { columnsList.add(columnDataToModel(columnsData))}
        }
        return columnsList
    }

    fun isColumnsExist(target : String, columns : List<PostgreColumn>) : Boolean {
        return columns.any { it.columnName == target }
    }

    fun showPgConfig() {
        println("Adresse du serveur PostgreSQL : "+ config.host)
        println("Port du serveur PostgreSQL : "+ config.port)
        println("Utilisateur d'accès au serveur : "+ config.username)
        println("Mot de passe de l'utilsateur : "+ config.password)
        println("Base de données cible : "+ config.database)
        println("Schéma cible : "+ config.schema)
        println("Table cible : "+ config.table)
    }

    private fun columnDataToModel(resultSet: ResultSet) : PostgreColumn {
        return PostgreColumn(
            columnName = resultSet.getString("COLUMN_NAME"),
            columnType = resultSet.getString("TYPE_NAME"),
            jdbcColumnType = resultSet.getInt("DATA_TYPE"),
            columnSize = resultSet.getInt("COLUMN_SIZE"),
            columnIsNullable = resultSet.getString("IS_NULLABLE")
        )
    }
}