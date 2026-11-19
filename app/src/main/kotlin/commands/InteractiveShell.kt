package net.unotec.app.commands

import org.jline.reader.EndOfFileException
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.terminal.TerminalBuilder
import java.nio.charset.StandardCharsets

class InteractiveShell(private val factory: CommandFactory) {

    fun run() {
        TerminalBuilder.builder().name("tcHaoFSQL").system(true).color(true).encoding(StandardCharsets.UTF_8.toString())
            .build().use { terminal ->
            val reader = LineReaderBuilder.builder().terminal(terminal).build()

            while (true) {
                try {
                    val input = reader.readLine("tcHaoFSQL > ").trim()
                    if (input.isEmpty()) continue
                    if (input.equals("exit", true)) break

                    val arguments = reader.parser.parse(input, input.length).words().toTypedArray()


                } catch (e: UserInterruptException) {
                    continue
                } catch (e: EndOfFileException) {
                    break
                }
            }
        }
    }
}