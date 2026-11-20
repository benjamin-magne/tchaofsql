package net.unotec.app.commands.pg

import picocli.CommandLine.Command

@Command(name = "pg", mixinStandardHelpOptions = true, description = ["Chargement et écriture PostgreSQL"])
class PgRootCommand
