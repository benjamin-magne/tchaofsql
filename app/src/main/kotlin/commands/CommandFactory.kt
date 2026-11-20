package net.unotec.app.commands

import net.unotec.app.commands.db.DbRootCommand
import net.unotec.app.commands.db.check.DbCheckCommand
import net.unotec.app.commands.db.config.DbConfigCommands
import net.unotec.app.commands.xml.XmlRootCommand
import net.unotec.app.commands.xml.file.XmlFileCommand
import net.unotec.app.commands.clear.ClearCommand
import net.unotec.app.commands.xml.columns.XmlColumnsCommands
import net.unotec.app.commands.xml.records.XmlRecordsCommands
import net.unotec.app.commands.pg.PgRootCommand
import net.unotec.app.commands.pg.columns.PgColumnsCommands
import net.unotec.app.commands.pg.defaults.PgDefaultsCommands
import net.unotec.app.commands.pg.execute.PgExecuteCommand
import net.unotec.app.commands.map.MapRootCommand
import net.unotec.app.commands.map.columns.MapColumnsCommands
import net.unotec.app.commands.map.pk.MapPkCommands
import net.unotec.app.services.SessionService
import picocli.CommandLine

class CommandFactory(val session: SessionService) {
    private val context = CommandContext(session)

    fun create(): CommandLine {
        val root = CommandLine(RootCommand())
        root.addSubcommand(CommandLine(DbRootCommand())
            .addSubcommand(DbConfigCommands(session) {
                context.data.postgreColumns = emptyList()
                context.data.defaultValues = emptyMap()
                context.resetMappings()
            })
            .addSubcommand(DbCheckCommand(session)))
        root.addSubcommand(CommandLine(XmlRootCommand())
            .addSubcommand(XmlFileCommand(context))
            .addSubcommand(XmlColumnsCommands(context))
            .addSubcommand(XmlRecordsCommands(context)))
        root.addSubcommand(CommandLine(PgRootCommand())
            .addSubcommand(PgExecuteCommand(context))
            .addSubcommand(PgColumnsCommands(context))
            .addSubcommand(PgDefaultsCommands(context)))
        root.addSubcommand(CommandLine(MapRootCommand())
            .addSubcommand(MapColumnsCommands(context))
            .addSubcommand(MapPkCommands(context)))
        root.addSubcommand(ClearCommand(context))
        fun configure(command: CommandLine) {
            if (command.commandSpec.userObject() !is java.lang.reflect.Method) command.commandSpec.mixinStandardHelpOptions(true)
            command.setExecutionExceptionHandler { error, cmd, _ ->
                cmd.err.println("Erreur : ${error.message ?: error.javaClass.simpleName}")
                1
            }
            command.subcommands.values.forEach { configure(it) }
        }
        configure(root)
        return root
    }
}
