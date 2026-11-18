package net.unotec.app.interfaces

import net.unotec.app.models.MappedColumn
import net.unotec.app.models.PostgreColumnMetada
import java.sql.Connection
import java.sql.PreparedStatement

interface ISqlWriterService {

    fun prepareUpsertStatement(
        connection: Connection,
        mappedColumns : List<MappedColumn>,
        mappedRow : Map<PostgreColumnMetada, String>,
        pkColumn : String
    ) : PreparedStatement

    fun baseSqlScript() : String

    fun mappedColumnsToParameters(mappedColumns : List<MappedColumn>) : String

    fun mappedColumnsToValues(mappedColumns : List<MappedColumn>) : String

    fun mappedColumnsToUpdates(mappedColumns : List<MappedColumn>, pkColumn : String) : String


}