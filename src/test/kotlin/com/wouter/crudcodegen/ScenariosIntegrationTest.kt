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
        listOf(
                listOf("Patient", "lastName:string", "firstName:string"),
                listOf("QuestionForm", "name:string", "url:string"),
                listOf("PatientQuestionForm", "patientId:Patient", "questionFormId:QuestionForm")
        ).forEach {
            generate("entity", *it.toTypedArray())
            generate("service", *it.toTypedArray())
            generate("graphql", *it.toTypedArray())
            generate("controller", *it.toTypedArray())
        }
        assertThat(executeTests(tempDir), equalTo(true))
    }

    private fun generate(vararg args: String) {
        if (!commandLineInterface.interpret(tempDir, args.toList())) {
            throw RuntimeException("Interpreter failed.")
        }
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