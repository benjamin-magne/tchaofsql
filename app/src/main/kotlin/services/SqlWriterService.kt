package net.unotec.app.services

import net.unotec.app.interfaces.ISqlWriterService
import net.unotec.app.models.MappedColumn
import net.unotec.app.models.PostgreColumnMetada
import java.sql.Connection
import java.sql.PreparedStatement

class SqlWriterService : ISqlWriterService {

    private fun quoteIdentifier(identifier: String): String {
        require(identifier.isNotBlank())
        return "\"" + identifier.replace("\"", "\"\"") + "\""
    }

    override fun prepareUpsertStatement(
        connection: Connection,
        mappedColumns: List<MappedColumn>,
        mappedRow: Map<PostgreColumnMetada, String>,
        pkColumn: String
    ): PreparedStatement {
        TODO("Not yet implemented")
    }

    override fun baseSqlScript(): String {
        TODO("Not yet implemented")
    }

    override fun mappedColumnsToParameters(mappedColumns: List<MappedColumn>): String { return mappedColumns.joinToString(", ") { it.postgreColumn.name } }

    override fun mappedColumnsToValues(mappedColumns: List<MappedColumn>): String { return List(mappedColumns.size) { "?" }.joinToString(", ") }

    override fun mappedColumnsToUpdates(mappedColumns: List<MappedColumn>, pkColumn: String): String {
        TODO("Not yet implemented")
    }
}