package com.wouter.crudcodegen

import com.wouter.crudcodegen.application.CommandLineInterface
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File

@SpringBootApplication
class CrudcodegenApplication(
        private val commandLineInterface: CommandLineInterface
): CommandLineRunner {
    override fun run(vararg args: String) {
        commandLineInterface.interpret(File(System.getProperty("user.dir")),
                args.toList())
    }
}

fun main(args: Array<String>) {
    runApplication<CrudcodegenApplication>(*args)
}