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
    fun `User creates a simple blog with a post`() {
        generate("new", "com.blogcorp", "blog")
        generate("entity", "Post", "title:string", "description:string")
        assertThat(executeTests(tempDir), equalTo(true))
    }

    private fun generate(vararg args: String) {
        commandLineInterface.interpret(tempDir, args.toList())
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