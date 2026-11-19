package net.unotec.app.interfaces

import net.unotec.app.models.DatabaseConfiguration
import net.unotec.app.models.MappedColumn
import net.unotec.app.models.PostgreColumnMetada
import java.sql.Connection
import java.sql.PreparedStatement

interface ISqlWriterService {

    fun prepareUpsertStatement(
        mappedColumns : List<MappedColumn>,
        mappedRow : Map<PostgreColumnMetada, String>,
        pkColumn : String
    ) : PreparedStatement

    fun baseSqlScript(mappedColumns : List<MappedColumn>, pkColumn: String) : String

    fun mappedColumnsToParameters(mappedColumns : List<MappedColumn>) : String

    fun mappedColumnsToValues(mappedColumns : List<MappedColumn>) : String

    fun mappedColumnsToUpdates(mappedColumns : List<MappedColumn>, pkColumn : String) : String


}