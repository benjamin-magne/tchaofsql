package net.unotec.app

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import net.unotec.app.commands.RootCommand
import net.unotec.app.commands.xml.RootXmlCommand
import net.unotec.app.commands.xml.load.LoadFileCommand
import net.unotec.app.commands.xml.load.RootLoadXmlCommand
import net.unotec.app.data.XmlServiceData
import net.unotec.app.services.XmlService

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args: Array<String>) {

    val xmlService = XmlService()

    var xmlServiceData = XmlServiceData()

    RootCommand().subcommands(RootXmlCommand().subcommands(RootLoadXmlCommand().subcommands(LoadFileCommand(xmlService)))).main(args)
}
