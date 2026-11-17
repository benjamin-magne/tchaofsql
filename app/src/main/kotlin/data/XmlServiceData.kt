package net.unotec.app.data

import javax.xml.stream.XMLStreamReader

data class XmlServiceData(
    var listColumns: Set<String> = emptySet(),
    var loadRecord : List<Map<String, String>> = emptyList(),
    var xmlStreamReader: XMLStreamReader
)
