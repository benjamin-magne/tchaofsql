package net.unotec.app.services

import net.unotec.app.interfaces.ISessionService
import net.unotec.app.models.DatabaseConfiguration
import net.unotec.app.models.MappedColumn
import net.unotec.app.models.PostgreColumnMetada
import net.unotec.app.models.SessionData

class SessionService : ISessionService {

    private val sessionData: SessionData = SessionData()

    override fun getMappedColumns(): List<MappedColumn> { return sessionData.mappedColumns }

    override fun setMappedColumns(mappedColumns: List<MappedColumn>) { sessionData.mappedColumns = mappedColumns }

    override fun getMappedRows(): List<Map<PostgreColumnMetada, String>> { return sessionData.mappedRows }

    override fun setMappedRows(mappedRows: List<Map<PostgreColumnMetada, String>>) { sessionData.mappedRows = mappedRows }

    override fun getConfiguration(): DatabaseConfiguration { return sessionData.configuration }

    override fun setConfiguration(configuration: DatabaseConfiguration) { sessionData.configuration = configuration }

    override fun getXmlPath(): String { return sessionData.xmlPath }

    override fun setXmlPath(xmlPath: String) { sessionData.xmlPath = xmlPath }
}