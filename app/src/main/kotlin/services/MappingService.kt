package net.unotec.app.services

class MappingService {

    fun mapColumn(source : String, target : String) : Map<String, String> {
        return mapOf(source to target)
    }
}