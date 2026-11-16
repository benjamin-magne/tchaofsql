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