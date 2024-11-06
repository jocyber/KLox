package klox

import java.io.File
import klox.parser.Parser
import klox.tokenizer.Scanner

object KLox {
    // TODO: add a REPL
    @JvmStatic
    fun main(args: Array<String>) {
        val sourceFile = File(args[1])
        val tokens = Scanner(sourceFile.readText()).scan()
        val ast = Parser(tokens).parse()

        println(ast)
    }
}
