package net.unotec.app.models

import net.unotec.app.services.MapperService

data class SessionData(
    var mappedColumns: List<MappedColumn> = listOf(),
    var mappedRows: List<Map<PostgreColumnMetada, String>> = listOf(),
    var configuration: DatabaseConfiguration = DatabaseConfiguration("",0,"","","","",""),
    var xmlPath: String = ""
)
