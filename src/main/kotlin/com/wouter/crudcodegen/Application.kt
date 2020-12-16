package com.wouter.crudcodegen

import com.github.jknack.handlebars.EscapingStrategy
import com.github.jknack.handlebars.EscapingStrategy.NOOP
import com.github.jknack.handlebars.Handlebars
import com.wouter.crudcodegen.application.CommandLineInterface
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication.exit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import picocli.CommandLine
import picocli.CommandLine.IFactory
import kotlin.system.exitProcess

@SpringBootApplication
class Application(
        private val commandLineInterface: CommandLineInterface,
        private val factory: IFactory
): CommandLineRunner, ExitCodeGenerator {
    private var _exitCode: Int = 0

    override fun run(vararg args: String) {
        _exitCode = CommandLine(commandLineInterface, factory).execute(*args)
    }

    override fun getExitCode(): Int {
        return _exitCode
    }
}

fun main(args: Array<String>) {
    exitProcess(exit(runApplication<Application>(*args)))
}
