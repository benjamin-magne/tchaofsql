package net.unotec.app.interfaces

import net.unotec.app.models.MappedColumn
import net.unotec.app.models.PostgreColumnMetada

interface IMapperService {

    fun mapColumn(xmlColumn : String, postgreColumn : PostgreColumnMetada) : MappedColumn
    fun mapRows(xmlRow : Map<String, String>, mappedColumns : List<MappedColumn>) : Map<PostgreColumnMetada, String>
    fun setPkColumn(xmlColumn: String, mappedColumns: List<MappedColumn>) : String

}