package net.unotec.app.services

import net.unotec.app.interfaces.IMapperService
import net.unotec.app.models.MappedColumn
import net.unotec.app.models.PostgreColumnMetada

class MapperService : IMapperService {

    typealias MappedRow = Map<PostgreColumnMetada, String>
    typealias XmlRow = Map<String, String>

    override fun mapColumn(xmlColumn: String, postgreColumn: PostgreColumnMetada): MappedColumn {
        return MappedColumn(xmlColumn, postgreColumn)
    }

    override fun mapRows(xmlRow: XmlRow, mappedColumns: List<MappedColumn>): MappedRow {
        return mappedColumns.associate { mapping ->
            val value = xmlRow[mapping.xmlColumn]
                ?: throw IllegalArgumentException(
                    "Colonne XML absente : ${mapping.xmlColumn}"
                )

            mapping.postgreColumn to value
        }
    }

    override fun setPkColumn(xmlColumn: String, mappedColumns: List<MappedColumn>): String {
        require( xmlColumn.isNotBlank() ) { "Une colonne XML doit être renseignée" }
        require( mappedColumns.any { it.xmlColumn==xmlColumn }  ) { "La colonne spécifiée n'est pas dans la liste des colonnes mappées"}
        return xmlColumn
    }
}