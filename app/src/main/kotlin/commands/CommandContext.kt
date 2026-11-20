package net.unotec.app.commands

import net.unotec.app.models.SessionData
import net.unotec.app.services.*

/** State shared by every command, including successive interactive shell invocations. */
class CommandContext(val session: SessionService) {
    val data = SessionData()
    val files = FileLoaderService()
    val xml = XmlReaderService()
    val database = DatabaseService()
    val reader = PostgreReaderService()
    val mapper = MapperService()
    val writer = PostgreWriterService()

    fun invalidateRows() = session.setMappedRows(emptyList())

    fun resetMappings() {
        session.setMappedColumns(emptyList())
        invalidateRows()
        data.primaryKey = null
    }

    fun clear() {
        resetMappings()
        session.setXmlPath("")
        session.setConfiguration(SessionData().configuration)
        data.xmlColumns = emptySet()
        data.xmlRecords = emptyList()
        data.postgreColumns = emptyList()
        data.defaultValues = emptyMap()
    }
}

