package net.unotec.app.services

import net.unotec.app.config.PgConfiguration
import net.unotec.app.models.MappedPgColumnWithXmlValue
import net.unotec.app.models.PgColumn
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class PgService(private val config: PgConfiguration) {

    fun connect() : Connection{
        val url = "jdbc:postgresql://${config.host}:${config.port}/${config.database}"
        return DriverManager.getConnection(url, config.username, config.password)
    }

    fun getColumnsData(connection: Connection, columnName : String? = null) : List<PgColumn> {
        val columnsList = mutableListOf<PgColumn>()
        connection.metaData.getColumns(null, config.schema, config.table, columnName).use { columnsData ->
            while (columnsData.next()) { columnsList.add(columnDataToModel(columnsData))}
        }
        return columnsList
    }

    fun executeBatch(connection: Connection, statement: PreparedStatement, dryRun: Boolean = false) : IntArray {
        connection.autoCommit = false
        return try {
            val results = statement.executeBatch()
            if (dryRun) { connection.rollback() } else { connection.commit() }
            results
        } catch (e: Exception) {
            connection.rollback()
            throw e
        }
    }

    fun prepareStatement(connection: Connection, sql : String) : PreparedStatement { return connection.prepareStatement(sql) }

    private fun isColumnsExist(target : String, columns : List<PgColumn>) : Boolean {
        return columns.any { it.columnName == target }
    }

    private fun columnDataToModel(resultSet: ResultSet) : PgColumn {
        return PgColumn(
            columnName = resultSet.getString("COLUMN_NAME"),
            jdbcColumnType = resultSet.getInt("DATA_TYPE"),
            columnIsNullable = resultSet.getString("IS_NULLABLE")
        )
    }
}