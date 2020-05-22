package com.wouter.crudcodegen.application

import com.wouter.crudcodegen.generators.Generator
import org.springframework.stereotype.Service
import java.io.File

@Service
class CommandLineInterface(
        private val generatorExecutor: GeneratorExecutor,
        private val generators: List<Generator>
) {
    fun interpret(currentPath: File, args: List<String>) {
        showAbout()
        if (args.isEmpty()) {
            showSyntax()
            return
        }

        if (!generatorExecutor.execute(currentPath, args[0], args.drop(1))) {
            showSyntax()
            println("No generator accepts the \"${args[0]}\" command.")
            return
        }
    }

    private fun showAbout() {
        println("Crud CodeGen")
        println("  Generate all your CRUDs in an instant!")
        println()
    }

    private fun showSyntax() {
        println("Syntax")

        generators.forEach {
            println("  ${it.getSyntax()}")
            println("    ${it.getSyntaxDescription()}")
            println()
        }
    }
}