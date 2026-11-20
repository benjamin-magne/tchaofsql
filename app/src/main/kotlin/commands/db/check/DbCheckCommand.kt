package net.unotec.app.commands.db.check

import net.unotec.app.services.DatabaseService
import net.unotec.app.services.SessionService
import picocli.CommandLine.Command

@Command(mixinStandardHelpOptions = true, name = "check")
class DbCheckCommand(val session: SessionService) : Runnable {
    private val service = DatabaseService()

    override fun run() {
        val config = session.getConfiguration()

        val check = service.checkConfiguration(config)
        if (check) { println("OK") } else { println("KO") }

        val test = service.connect(config).use { it.isValid(5) }
        if (test) { println("Connexion OK") } else { println("Connexion KO") }
    }
}

