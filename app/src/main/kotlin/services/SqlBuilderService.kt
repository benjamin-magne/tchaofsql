package net.unotec.app.services

import net.unotec.app.config.PostgreConfig
import net.unotec.app.models.PostgreColumn
import java.sql.Connection
import java.sql.PreparedStatement

class SqlBuilderService(private val config: PostgreConfig)  {


    fun buildBaseScript(mappedColumns: Map<String, String>, pgColumnsList: List<PostgreColumn>) : String{

        return """
            INSERT INTO ${config.schema}.${config.table} (${columnsOnSqlFormat(mappedColumns)})
            VALUES (${valuesOnSqlFormat(mappedColumns)})
        """.trimIndent()
    }

    /*
    fun bindValues(
        statement: PreparedStatement,
        xmlRecord: Map<String, String>,
        mappedColumns: Map<String, String>,
        pgColumnsList: List<PostgreColumn>,
        typeService: TypeService
    ) {
        mappedColumns.forEachIndexed { index, (xmlColumn, pgColumn) ->

            // 1. Récupérer la valeur XML
            val xmlValue = xmlRecord[xmlColumn]
                ?: error("Colonne XML introuvable : $xmlColumn")

            // 2. Récupérer les métadonnées PostgreSQL
            val column = pgColumnsList.find { it.name == pgColumn }
                ?: error("Colonne PostgreSQL introuvable : $pgColumn")

            // 3. Convertir la valeur XML
            val convertedValue = typeService.convert(
                xmlValue,
                column.dataType
            )

            // 4. Affecter la valeur au bon paramètre SQL
            statement.setObject(index + 1, convertedValue)
        }
    }
    */

    fun prepareStatement(connection: Connection, sql : String) : PreparedStatement {
        return connection.prepareStatement(sql)
    }

    private fun columnsOnSqlFormat(mappedColumns: Map<String, String>) : String {
        return mappedColumns.values.joinToString(",")
    }

    private fun valuesOnSqlFormat(mappedColumns: Map<String, String>) : String {
        return  mappedColumns.values.joinToString(",") { "?" }
    }

}