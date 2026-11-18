package net.unotec.app.interfaces

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.xml.stream.XMLStreamReader

interface IFileLoaderService {

    fun loadXmlFile (filePath: String) : FileInputStream
}