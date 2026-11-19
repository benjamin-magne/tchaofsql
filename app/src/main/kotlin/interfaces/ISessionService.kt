package net.unotec.app.interfaces

import net.unotec.app.models.DatabaseConfiguration
import net.unotec.app.models.MappedColumn
import net.unotec.app.models.PostgreColumnMetada
import net.unotec.app.models.SessionData

interface ISessionService {

    //Colonnes mappées
    fun getMappedColumns(): List<MappedColumn>
    fun setMappedColumns(mappedColumns: List<MappedColumn>)

    //Enregistrements mappés
    fun getMappedRows(): List<Map<PostgreColumnMetada, String>>
    fun setMappedRows(mappedRows: List<Map<PostgreColumnMetada, String>>)

    //Configuration
    fun getConfiguration(): DatabaseConfiguration
    fun setConfiguration(configuration: DatabaseConfiguration)

    //Chemin XML
    fun getXmlPath(): String
    fun setXmlPath(xmlPath: String)
}