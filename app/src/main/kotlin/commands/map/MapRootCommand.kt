package net.unotec.app.commands.map

import picocli.CommandLine.Command
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

@Command(name = "map", mixinStandardHelpOptions = true)
class MapRootCommand : Runnable {
    @Spec
    lateinit var spec: CommandSpec

    override fun run() { spec.commandLine().usage(System.out) }
}
