package net.unotec.app.interfaces

import java.sql.Connection
import java.sql.PreparedStatement

interface IPostgreWriterService {
    fun executeUpsertStatement(connection : Connection, statement: PreparedStatement, dryRun : Boolean = true) : Int
}