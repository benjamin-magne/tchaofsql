package net.unotec.app.commands.db.config

import net.unotec.app.services.SessionService
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(mixinStandardHelpOptions = true, name = "config")
class DbConfigCommands(val session: SessionService, private val changed: () -> Unit = {}) {
    @Command(mixinStandardHelpOptions = true, name = "set")
    fun config(
        @Option(names = ["--host"]) host: String?,
        @Option(names = ["--port"]) port: Int?,
        @Option(names = ["--username"]) username: String?,
        @Option(names = ["--password"]) password: String?,
        @Option(names = ["--database"]) database: String?,
        @Option(names = ["--schema"]) schema: String?,
        @Option(names = ["--table"]) table: String?
    ) {
        require(port == null || port in 1..65535) { "Port invalide." }
        val old = session.getConfiguration()
        val config = old.copy(
            host = host ?: old.host.ifBlank { "127.0.0.1" },
            port = port ?: old.port.takeIf { it > 0 } ?: 5432,
            username = username ?: old.username.ifBlank { "postgres" },
            password = password ?: old.password,
            database = database ?: old.database.ifBlank { "postgres" },
            schema = schema ?: old.schema.ifBlank { "public" },
            table = table ?: old.table
        )
        if (config != old) { session.setConfiguration(config); changed() }
        println("Configuration enregistrée.")
    }

    @Command(mixinStandardHelpOptions = true, name = "show")
    fun show() { println(session.getConfiguration().copy(password = "********")) }
}

