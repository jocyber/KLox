package klox

import java.io.File
import java.net.URI
import klox.tokenizer.Scanner

object KLox {
    // TODO: add a REPL
    @JvmStatic
    fun main(args: Array<String>) {
        val sourceFile = File(args[1])
        val tokens = Scanner(sourceFile.readText()).scan()

        tokens.forEach {
            println("${it.type} ${it.value?.let { "-> $it" } ?: ""}")
        }
    }
}
