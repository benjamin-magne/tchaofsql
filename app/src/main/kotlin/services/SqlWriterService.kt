package net.unotec.app.services

import net.unotec.app.interfaces.ISqlWriterService
import net.unotec.app.models.DatabaseConfiguration
import net.unotec.app.models.MappedColumn
import net.unotec.app.models.PostgreColumnMetada
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class SqlWriterService(private val connection: Connection, private val configuration: DatabaseConfiguration) : ISqlWriterService {

    override fun prepareUpsertStatement(
        mappedColumns: List<MappedColumn>,
        mappedRow: Map<PostgreColumnMetada, String>,
        pkColumn: String
    ): PreparedStatement {
        val statement = try { connection.prepareStatement(baseSqlScript( mappedColumns, pkColumn)) } catch (e: SQLException) {
            println("Erreur SQL : " + e.message)
            throw e
        }
        bindParameters(statement, mappedColumns, mappedRow)
        return statement
    }

    override fun baseSqlScript(mappedColumns : List<MappedColumn>, pkColumn: String): String {
        return """
            INSERT INTO ${quoteIdentifier(configuration.schema)}.${quoteIdentifier(configuration.table)} 
            (${mappedColumnsToParameters(mappedColumns)})
            VALUES (${mappedColumnsToValues(mappedColumns)})
            ${mappedColumnsToUpdates(mappedColumns, pkColumn)}
        """.trimIndent()
    }

    override fun mappedColumnsToParameters(mappedColumns: List<MappedColumn>): String { return mappedColumns.joinToString(", ") { quoteIdentifier(it.postgreColumn.name) } }

    override fun mappedColumnsToValues(mappedColumns: List<MappedColumn>): String { return List(mappedColumns.size) { "?" }.joinToString(", ") }

    override fun mappedColumnsToUpdates(mappedColumns: List<MappedColumn>, pkColumn: String): String {
        val updateColumns = mappedColumns.filter { it.postgreColumn.name != pkColumn }
        val updateClause = updateColumns.joinToString(", ") {
            val identifier = quoteIdentifier(it.postgreColumn.name)
            "$identifier = EXCLUDED.$identifier"
        }
        val conflict = quoteIdentifier(pkColumn)
        return if (updateColumns.isEmpty()) { "ON CONFLICT ($conflict) DO NOTHING" } else { "ON CONFLICT ($conflict) DO UPDATE SET $updateClause" }
    }

    private fun quoteIdentifier(identifier: String): String {
        require(identifier.isNotBlank())
        return "\"" + identifier.replace("\"", "\"\"") + "\""
    }

    private fun bindParameters(statement: PreparedStatement, mappedColumns: List<MappedColumn>, mappedRow: Map<PostgreColumnMetada, String>) {
        mappedColumns.forEachIndexed { index, column ->
            val value = mappedRow[column.postgreColumn]
            val jdbcCode = column.postgreColumn.jdbcType
            try { statement.setObject(index + 1, value, jdbcCode) } catch (e: Exception) {
                println("Erreur SQL : " + e.message)
                throw e
            }
        }
    }
}