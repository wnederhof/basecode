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
    fun `User scaffolds a book store`() {
        generate("new", "com.wouter", "bookstore")
//        generate("frontend", "com.wouter", "bookstore")
        listOf(
                listOf("Book", "title:string", "author:string")
        ).map { it.toTypedArray() }.forEach { args ->
            generate("entity", *args)
            generate("service", *args)
            generate("graphql", *args)
//            generate("frontend-scaffold", *args)
        }
        assertThat(executeBackendTests(tempDir), equalTo(true))
    }

    private fun generate(vararg args: String) {
        if (!commandLineInterface.interpret(tempDir, args.toList())) {
            throw RuntimeException("Interpreter failed.")
        }
    }

    private fun executeBackendTests(tempDir: File): Boolean {
        return OK_RETURN_STATUS == ProcessBuilder("sh", "mvnw", "verify")
                .inheritIO()
                .directory(File(tempDir.path + "/backend"))
                .start()
                .waitFor()
    }

    private companion object {
        const val OK_RETURN_STATUS = 0
    }
}
