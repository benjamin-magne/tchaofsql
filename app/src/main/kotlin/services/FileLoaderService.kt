package net.unotec.app.services

import net.unotec.app.interfaces.IFileLoaderService
import java.io.File
import java.io.FileInputStream

class FileLoaderService : IFileLoaderService {

    override fun loadXmlFile (filePath: String) : FileInputStream {
        return validateXmlFile(filePath).inputStream()
    }

    private fun isXmlFile(file: File): Boolean { return file.extension.equals("xml", true) }

    private fun validateXmlFile(filePath: String) : File {
        require(filePath.isNotBlank()) { "Le chemin du fichier ne peux pas être vide." }
        val file = File(filePath)
        require(file.isFile()) { "Le chemin donné ne correspond pas à un fichier existant." }
        require(isXmlFile(file)) { "Le fichier n'est pas au format XML." }
        require(file.length()!=0L) { "Le fichier est chargé mais vide." }
        return file
    }
}