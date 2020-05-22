package com.wouter.crudcodegen.application

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
internal class ProjectIntegrationTest {
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
    fun `Building using template "new" will succeed the integration tests`() {
        commandLineInterface.interpret(tempDir, listOf("new", "com.hello", "world"))
        assertTrue(executeTests(tempDir))
    }

    private fun executeTests(tempDir: File): Boolean {
        return OK_RETURN_STATUS == ProcessBuilder("sh", "mvnw", "verify")
                .inheritIO()
                .directory(tempDir)
                .start()
                .waitFor()
    }

    private companion object {
        const val OK_RETURN_STATUS = 0
    }
}