package com.wouter.crudcodegen.generator.project

import org.springframework.stereotype.Service
import java.io.File

@Service
class CommandLineInterpreter(
        private val projectFilesGenerator: ProjectFilesGenerator
) {
    fun interpret(currentPath: File, args: List<String>) {
        println("Syntax")
        println("  new <group> <artifactId>")
        println("    Generates new project called <artifactId>. For instance: new com.wouter example")
        println()

        if (args.isEmpty()) {
            return
        }

        when (args[0]) {
            "new" -> {
                val (groupId, artifactId) = args.drop(1)
                projectFilesGenerator.generateNew(currentPath, groupId, artifactId)
            }
            else ->
                println("Unrecognized input arguments: $args.")
        }
    }
}