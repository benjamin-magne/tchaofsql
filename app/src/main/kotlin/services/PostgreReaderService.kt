package net.unotec.app.services

import net.unotec.app.interfaces.IPostgreReaderService
import net.unotec.app.models.DatabaseConfiguration
import net.unotec.app.models.PgColumn
import net.unotec.app.models.PostgreColumnMetada
import java.sql.Connection
import java.sql.ResultSet

class PostgreReaderService : IPostgreReaderService {
    override fun getColumnsMetadata(connection: Connection, configuration: DatabaseConfiguration, columnName: String?): List<PostgreColumnMetada> {
        val columnsMetadataList = mutableListOf<PostgreColumnMetada>()
        connection.metaData.getColumns(
            null,
            configuration.schema,
            configuration.table,
            columnName
        ).use { it -> while (it.next()) { columnsMetadataList.add(columnMetadataToPostgreColumnMetadata(it)) } }
        return columnsMetadataList
    }

    private fun columnMetadataToPostgreColumnMetadata(resultSet: ResultSet) : PostgreColumnMetada {
        return PostgreColumnMetada(
            name = resultSet.getString("COLUMN_NAME"),
            size = resultSet.getInt("SIZE"),
            postgreType = resultSet.getString("TYPE_NAME"),
            jdbcType = resultSet.getInt("DATA_TYPE"),
            isNullable = resultSet.getString("IS_NULLABLE")
        )
    }
}