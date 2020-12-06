package com.wouter.crudcodegen

import com.wouter.crudcodegen.application.CommandLineInterface
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File

@SpringBootApplication
class Application(
        private val commandLineInterface: CommandLineInterface
): CommandLineRunner {
    override fun run(vararg args: String) {
        try {
            commandLineInterface.interpret(File(System.getProperty("user.dir")),
                    args.toList())
        } catch(e: IllegalStateException) {
            println(e.message)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
