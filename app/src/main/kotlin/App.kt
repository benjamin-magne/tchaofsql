package net.unotec.app

import net.unotec.app.commands.CommandFactory
import net.unotec.app.commands.InteractiveShell
import net.unotec.app.services.SessionService

fun main(args: Array<String>) {

    val session = SessionService()
    val factory = CommandFactory(session)
    val shell = InteractiveShell(factory)

    if (args.isEmpty()) shell.run()
    else kotlin.system.exitProcess(factory.create().execute(*args))
}
