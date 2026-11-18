package net.unotec.app.interfaces

import net.unotec.app.models.DatabaseConfiguration
import net.unotec.app.models.PostgreColumnMetada
import java.sql.Connection

interface IPostgreReaderService {
    fun getColumnsMetadata(connection: Connection,configuration: DatabaseConfiguration, columnName: String? = null) : List<PostgreColumnMetada>
}