package net.unotec.app.commands

import io.mockk.*
import net.unotec.app.commands.pg.execute.PgExecuteCommand
import picocli.CommandLine
import net.unotec.app.models.*
import net.unotec.app.services.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Savepoint
import java.sql.SQLException

class CommandsTest {
    @Test
    fun `tree and XML state survive successive invocations`() {
        val session = SessionService()
        val factory = CommandFactory(session)
        val path = java.io.File(javaClass.getResource("/data.xml")!!.toURI()).path
        fun run(vararg args: String) = factory.create().execute(*args)
        assertEquals(0, run("--help"))
        for (group in listOf("xml", "db", "pg", "map")) assertEquals(0, run(group, "--help"))
        assertEquals(0, run("xml", "file", "load", path))
        assertEquals(path, session.getXmlPath())
        assertEquals(0, run("xml", "columns", "load"))
        assertEquals(0, run("xml", "records", "load"))
        assertEquals(0, run("xml", "records", "add", "IDPERSONNE=4", "NOM=Avec espaces"))
        assertEquals(0, run("xml", "records", "remove", "4"))
        assertEquals(1, run("xml", "records", "remove", "99"))
        assertEquals(0, run("clear"))
        assertEquals("", session.getXmlPath())
        assertEquals(1, run("xml", "columns", "load"))
    }

    @Test
    fun `partial configuration preserves existing fields including password`() {
        val session = SessionService()
        val factory = CommandFactory(session)
        assertEquals(0, factory.create().execute("db", "config", "set", "--password", "secret", "--table", "people"))
        assertEquals(0, factory.create().execute("db", "config", "set", "--host", "localhost"))
        assertEquals("secret", session.getConfiguration().password)
        assertEquals("people", session.getConfiguration().table)
    }

    @Test
    fun `import controls transaction around all rows`() {
        for (mode in listOf("rollback", "atomic", "partial")) {
            mockkConstructor(DatabaseService::class)
            try {
                val connection = mockk<Connection>(relaxed = true)
                val statement = mockk<PreparedStatement>(relaxed = true)
                val savepoint = mockk<Savepoint>()
                every { anyConstructed<DatabaseService>().connect(any()) } returns connection
                every { connection.prepareStatement(any<String>()) } returns statement
                every { connection.setSavepoint() } returns savepoint
                every { statement.executeUpdate() } returnsMany listOf(1, 1)
                if (mode != "rollback") every { statement.executeUpdate() } returns 1 andThenThrows SQLException("invalid row")
                val ctx = CommandContext(SessionService())
                val column = PostgreColumnMetada("id", 0, "int4", java.sql.Types.INTEGER, "NO")
                ctx.session.setConfiguration(DatabaseConfiguration("localhost", 5432, "user", "pass", "db", "public", "people"))
                ctx.session.setMappedColumns(listOf(MappedColumn("ID", column)))
                ctx.data.primaryKey = "ID"
                ctx.data.xmlRecords = listOf(mapOf("ID" to "1"), mapOf("ID" to "2"))
                val result = CommandLine(PgExecuteCommand(ctx)).execute(when (mode) { "partial" -> "--commitiferror"; "atomic" -> "--nocommitiferror"; else -> "--rollback" })
                assertEquals(if (mode == "rollback") 0 else 1, result)
                verify(exactly = if (mode == "partial") 1 else 0) { connection.commit() }
                verify(exactly = if (mode == "partial") 0 else 1) { connection.rollback() }
                verify(exactly = if (mode == "rollback") 0 else 1) { connection.rollback(savepoint) }
            } finally { unmockkConstructor(DatabaseService::class) }
        }
    }
}
