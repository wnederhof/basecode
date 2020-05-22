package com.wouter.crudcodegen.generator

import com.wouter.crudcodegen.generator.project.CommandLineInterpreter
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File

@SpringBootApplication
class CrudcodegenApplication(
        private val commandLineInterpreter: CommandLineInterpreter
): CommandLineRunner {
    override fun run(vararg args: String) {
        commandLineInterpreter.interpret(File(System.getProperty("user.dir")),
                args.toList())
    }
}

fun main(args: Array<String>) {
    runApplication<CrudcodegenApplication>(*args)
}