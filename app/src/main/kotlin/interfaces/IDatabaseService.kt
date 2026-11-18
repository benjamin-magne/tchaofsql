package net.unotec.app.interfaces

import net.unotec.app.models.DatabaseConfiguration
import java.sql.Connection

interface IDatabaseService {
    fun connect(configuration: DatabaseConfiguration) : Connection

    fun testConnect(configuration: DatabaseConfiguration) : Boolean

    fun checkConfiguration(configuration: DatabaseConfiguration) : Boolean
}