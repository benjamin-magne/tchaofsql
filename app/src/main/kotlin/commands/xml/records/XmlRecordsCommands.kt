package net.unotec.app.commands.xml.records

import net.unotec.app.commands.CommandContext
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters

@Command(mixinStandardHelpOptions = true, name = "records", description = ["Enregistrements XML ; indices Ã  partir de 1"])
class XmlRecordsCommands(private val ctx: CommandContext) {
    @Command(mixinStandardHelpOptions = true, name = "load")
    fun load() {
        ctx.data.xmlRecords = ctx.files.loadXmlFile(ctx.session.getXmlPath()).use { ctx.xml.loadRecords(it) }
        ctx.invalidateRows()
        println("${ctx.data.xmlRecords.size} enregistrement(s) chargÃ©(s).")
    }

    @Command(mixinStandardHelpOptions = true, name = "show")
    fun show() {
        ctx.data.xmlRecords.forEachIndexed { i, row -> println("${i + 1}: $row") }
        println("${ctx.data.xmlRecords.size} enregistrement(s).")
    }

    @Command(mixinStandardHelpOptions = true, name = "add", description = ["Ajouter une ligne : colonne=valeur ..."])
    fun add(@Parameters(arity = "1..*", paramLabel = "COLUMN=VALUE") values: List<String>) {
        val pairs = values.map {
            require('=' in it) { "Valeur attendue : colonne=valeur" }
            it.substringBefore('=') to it.substringAfter('=')
        }
        require(pairs.map { it.first }.distinct().size == pairs.size) { "Colonne en double." }
        require(pairs.all { it.first in ctx.data.xmlColumns }) { "Charger les colonnes XML avant d'ajouter une ligne." }
        val row = pairs.toMap()
        ctx.mapper.mapRows(row, ctx.session.getMappedColumns())
        ctx.data.xmlRecords = ctx.data.xmlRecords + row
        ctx.invalidateRows()
    }

    @Command(mixinStandardHelpOptions = true, name = "remove")
    fun remove(@Parameters(paramLabel = "INDEX") index: Int) {
        require(index in 1..ctx.data.xmlRecords.size) { "Indice de ligne invalide." }
        ctx.data.xmlRecords = ctx.data.xmlRecords.filterIndexed { i, _ -> i != index - 1 }
        ctx.invalidateRows()
    }
}
