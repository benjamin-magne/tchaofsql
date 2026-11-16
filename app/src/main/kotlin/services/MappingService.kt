package net.unotec.app.services

import net.unotec.app.models.PostgreColumn

class MappingService(private val mappedColumns : MutableMap<String, String>) {

    fun add(source : String, target : String) {
        mappedColumns[source] = target
    }

    fun showMappedColumns() {
        mappedColumns.forEach { source, target ->
            println("$source (XML) -> $target (PostgreSQL)")
        }
    }

    fun removeAll() {
        mappedColumns.forEach { (source, target) -> mappedColumns.remove(source) }
    }

    fun removeMappedColumn(source: String) : Boolean {
        return mappedColumns.remove(source) != null
    }

    fun change(source: String, target: String): Boolean {
        if (!mappedColumns.containsKey(source)) return false

        mappedColumns[source] = target
        return true
    }

    fun isNullableColumnMapped(pgColumn : PostgreColumn) : Boolean {
        if (pgColumn.columnIsNullable == "YES" && !mappedColumns.containsKey(pgColumn.columnName)) { return false }
        return true
    }

}