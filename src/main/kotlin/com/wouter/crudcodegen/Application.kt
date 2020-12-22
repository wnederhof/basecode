package com.wouter.crudcodegen

import com.wouter.crudcodegen.application.CommandLineInterface
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication.exit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import picocli.CommandLine
import picocli.CommandLine.IFactory
import kotlin.system.exitProcess

@SpringBootApplication
class Application(
    private val commandLineInterface: CommandLineInterface,
    private val factory: IFactory
) : CommandLineRunner, ExitCodeGenerator {
    private var _exitCode: Int = 0

    override fun run(vararg args: String) {
        _exitCode = CommandLine(commandLineInterface, factory)
            .setExecutionExceptionHandler { exception, commandLine, parseResult ->
                commandLine.err.println(commandLine.colorScheme.errorText(exception.message))
                1
            }
            .execute(*args)
    }

    override fun getExitCode(): Int {
        return _exitCode
    }
}

fun main(args: Array<String>) {
    exitProcess(exit(runApplication<Application>(*args)))
}
