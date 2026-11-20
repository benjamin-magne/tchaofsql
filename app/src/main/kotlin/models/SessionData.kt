package net.unotec.app.models

import net.unotec.app.services.MapperService

data class SessionData(
    var mappedColumns: List<MappedColumn> = listOf(),
    var mappedRows: List<Map<PostgreColumnMetada, String>> = listOf(),
    var configuration: DatabaseConfiguration = DatabaseConfiguration("",0,"","","","",""),
    var xmlPath: String = "",
    var xmlColumns: Set<String> = emptySet(),
    var xmlRecords: List<Map<String, String>> = emptyList(),
    var postgreColumns: List<PostgreColumnMetada> = emptyList(),
    var primaryKey: String? = null,
    var defaultValues: Map<String, String> = emptyMap()
)
