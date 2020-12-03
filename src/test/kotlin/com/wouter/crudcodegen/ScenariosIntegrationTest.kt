package com.wouter.crudcodegen

import com.wouter.crudcodegen.application.CommandLineInterface
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

// Please note that the integration tests have only been tested on MacOS.
@SpringBootTest
internal class ScenariosIntegrationTest {
    @Autowired
    private lateinit var commandLineInterface: CommandLineInterface

    private lateinit var tempDir: File

    @BeforeEach
    fun setup() {
        tempDir = createTempDir("crudcodegen_")
    }

    @AfterEach
    fun teardown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `Integration test using all features builds without test errors`() {
        generate("new", "com.petty", "petstore")
        generate("frontend", "petstore")

        val ownerArgs = listOf("Owner", "name:string", "about:text", "age:int", "dateOfBirth:date", "timeOfBirth:dateTime")
        val petArgs = listOf("Pet", "ownerId:Owner", "name:string?", "about:text?", "age:int?", "dateOfBirth:date?", "timeOfBirth:dateTime?")

        listOf(ownerArgs, petArgs).map { it.toTypedArray() }.forEach { args ->
            generate("entity", *args)
            generate("service", *args)
            generate("graphql", *args)
            generate("frontend-scaffold", *args)
        }

        assertThat(executeBackendTests(tempDir), equalTo(true))
        assertThat(executeFrontendTests(tempDir), equalTo(true))
    }

    private fun generate(vararg args: String) {
        if (!commandLineInterface.interpret(tempDir, args.toList())) {
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
        return OK_RETURN_STATUS == ProcessBuilder("sh", "-c", "npm install && npm test")
                .inheritIO()
                .directory(File(tempDir.path))
                .start()
                .waitFor()
    }

    private companion object {
        const val OK_RETURN_STATUS = 0
    }
}
