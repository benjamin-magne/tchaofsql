package net.unotec.app.commands.xml.load

import com.github.ajalt.clikt.core.CliktCommand
import net.unotec.app.data.XmlServiceData
import net.unotec.app.services.XmlService

class LoadRecordsCommand(private val data : XmlServiceData, private val service : XmlService) : CliktCommand("records") {

    override fun run() {
        data.loadRecord = service.loadRecords(data.xmlStreamReader)
        echo(data.loadRecord.toString())
    }
}