package net.unotec.app.services

import net.unotec.app.interfaces.IPostgreWriterService
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class PostgreWriterService : IPostgreWriterService {
    override fun executeUpsertStatement(connection: Connection, statement: PreparedStatement, dryRun: Boolean) : Int {
        val connection = statement.connection
        connection.autoCommit = false

        return try {
            val affectedRows = statement.executeUpdate()

            if (dryRun) connection.rollback()
            else connection.commit()

            affectedRows
        } catch (e: SQLException) {
            connection.rollback()
            throw e
        }
    }
}