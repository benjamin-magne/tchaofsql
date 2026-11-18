package net.unotec.app.interfaces

import java.io.FileInputStream

interface IXmlReaderService {

    fun loadColumns(fileInputStream: FileInputStream) : Set<String>

    fun loadRecords(fileInputStream: FileInputStream) : List<Map<String, String>>

    fun isColumnsExist(fileInputStream: FileInputStream, column : String): Boolean
}