package net.unotec.app.services

import net.unotec.app.models.MappedPgColumnWithXmlValue
import net.unotec.app.models.PgColumn
import kotlin.collections.forEach

class MappingService(private val mappedColumns : MutableMap<String, String>) {

    fun add(source : String, target : String) {
        mappedColumns[source] = target
    }

    fun removeMappedColumn(source: String) : Boolean {
        return mappedColumns.remove(source) != null
    }

    fun change(source: String, target: String): Boolean {
        if (!mappedColumns.containsKey(source)) return false

        mappedColumns[source] = target
        return true
    }

    fun isNullableColumnMapped(pgColumn : PgColumn) : Boolean {
        if (pgColumn.columnIsNullable == "YES" && !mappedColumns.containsKey(pgColumn.columnName)) { return false }
        return true
    }

    fun mapXmlValuesWithPgColumns(xmlRecords : List<Map<String, String>>, pgColumnsList: List<PgColumn>) : List<MappedPgColumnWithXmlValue> {
        val mappedPgColumnWithXmlValues = mutableListOf<MappedPgColumnWithXmlValue>()
        //Parcourt des enregistrements XML
        xmlRecords.forEach { record ->
            //Parcourt des map
            record.forEach { xmlColumn, value ->
                val pgColumn = returnAssociatedPgColumn(xmlColumn, pgColumnsList)
                mappedPgColumnWithXmlValues.add(mapXmlValueWithPgColumn(value, pgColumn))
            }
        }
        return mappedPgColumnWithXmlValues
    }

    private fun returnAssociatedPgColumn(xmlColumnName : String, pgColumnsList: List<PgColumn>) : PgColumn {
        //On recherche la clé de mappedColumns avec xmlColumn, puis on renvoi le pgcolumn si le nom correspond
        val pgColumnName = mappedColumns[xmlColumnName]
            ?: throw IllegalArgumentException("Colonne XML non mappée : $xmlColumnName")
        return pgColumnsList.first {
            it.columnName == pgColumnName
        }
    }

    private fun mapXmlValueWithPgColumn(xmlValue : String, pgColumn: PgColumn): MappedPgColumnWithXmlValue {
        return MappedPgColumnWithXmlValue(pgColumn.columnName, xmlValue, pgColumn.jdbcColumnType)
    }

}