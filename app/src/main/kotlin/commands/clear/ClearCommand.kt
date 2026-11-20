package net.unotec.app.commands.clear

import net.unotec.app.commands.CommandContext
import picocli.CommandLine

@CommandLine.Command(name = "clear", description = ["Réinitialiser la session"])
class ClearCommand(private val context: CommandContext) : Runnable {
    override fun run() { context.clear(); println("Session réinitialisée.") }
}
