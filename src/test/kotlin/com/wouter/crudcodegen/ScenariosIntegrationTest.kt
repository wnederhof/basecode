package com.wouter.crudcodegen

import com.wouter.crudcodegen.application.CommandLineInterface
import com.wouter.crudcodegen.engine.FileManager
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import picocli.CommandLine
import java.io.File

// Please note that the integration tests have only been tested on MacOS.
@SpringBootTest
class ScenariosIntegrationTest {
    @Autowired
    private lateinit var commandLineInterface: CommandLineInterface

    @Autowired
    private lateinit var factory: CommandLine.IFactory

    @Autowired
    private lateinit var fileManager: FileManager

    private lateinit var tempDir: File

    @BeforeEach
    fun setup() {
        tempDir = createTempDir("crudcodegen_")
        fileManager.currentDir = tempDir.path
    }

    @AfterEach
    fun teardown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `Integration test using all features builds without test errors`() {
        executeCommand("new", "com.petty", "petstore")

        val projectContextDir = tempDir.path + "/petstore"

        fileManager.currentDir = projectContextDir

        executeCommand(
            "generate",
            "scaffold",
            "Owner",
            "name:string",
            "about:text",
            "age:int",
            "dateOfBirth:date",
            "timeOfBirth:datetime"
        )

        executeCommand(
            "generate",
            "scaffold",
            "Pet",
            "ownerId:Owner",
            "name:string_o",
            "about:text_o",
            "age:int_o",
            "dateOfBirth:date_o",
            "timeOfBirth:datetime_o"
        )

        assertThat(executeBackendTests(File(projectContextDir)), equalTo(true))
//        assertThat(executeFrontendTests(File(projectContextDir)), equalTo(true))
    }

    private fun executeCommand(vararg args: String) {
        val exitCode = CommandLine(commandLineInterface, factory).execute(*args)
        if (exitCode != 0) {
            throw RuntimeException("Interpreter failed.")
        }
    }

    private fun executeBackendTests(tempDir: File): Boolean {
        return OK_RETURN_STATUS == ProcessBuilder("sh", "mvnw", "verify")
            .inheritIO()
            .directory(File(tempDir.path))
            .start()
            .waitFor()
    }

    private fun executeFrontendTests(tempDir: File): Boolean {
        return OK_RETURN_STATUS == ProcessBuilder("sh", "-c", "npm install --legacy-peer-deps && npm test")
            .inheritIO()
            .directory(File(tempDir.path))
            .start()
            .waitFor()
    }

    private companion object {
        const val OK_RETURN_STATUS = 0
    }
}
