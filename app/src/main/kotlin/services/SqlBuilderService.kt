package net.unotec.app.services

import net.unotec.app.config.PgConfiguration

class SqlBuilderService(private val config: PgConfiguration)  {

    fun buildInsertSql(mappedColumns: Map<String, String>): String {
        val columns = mappedColumns.values.toList()
        require(columns.isNotEmpty()) { "Aucune colonne mappée" }

        val columnNames = columns.joinToString(", ") { quoteIdentifier(it) }

        val placeholders = List(columns.size) { "?" }.joinToString(", ")

        return """
            INSERT INTO ${quoteIdentifier(config.schema)}.${quoteIdentifier(config.table)}
            ($columnNames)
            VALUES ($placeholders)
        """.trimIndent()
    }

    fun buildUpsertSql(mappedColumns: Map<String, String>, conflictColumn: String): String {
        val columns = mappedColumns.values.toList()

        require(conflictColumn in columns) { "La clé UPSERT doit être présente dans le mapping" }

        val insertSql = buildInsertSql(mappedColumns)

        val updateColumns = columns.filter { it != conflictColumn }

        val updateClause = updateColumns.joinToString(", ") {
            val identifier = quoteIdentifier(it)
            "$identifier = EXCLUDED.$identifier"
        }

        val conflict = quoteIdentifier(conflictColumn)

        return if (updateColumns.isEmpty()) {
            "$insertSql ON CONFLICT ($conflict) DO NOTHING"
        } else {
            "$insertSql ON CONFLICT ($conflict) DO UPDATE SET $updateClause"
        }
    }

    private fun quoteIdentifier(identifier: String): String {
        require(identifier.isNotBlank())
        return "\"" + identifier.replace("\"", "\"\"") + "\""
    }
}