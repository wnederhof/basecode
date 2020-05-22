package com.wouter.crudcodegen.generator.project

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
    private lateinit var projectFilesGenerator: ProjectFilesGenerator

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
        projectFilesGenerator.generateNew(tempDir, "com.hello", "world")
        assertTrue(executeTests(tempDir))
    }

    @Test
    @Disabled // Not Implemented Yet.
    fun `Building using template "new" and "scaffold" will succeed the integration tests`() {
        projectFilesGenerator.generateNew(tempDir, "com.hello", "world")
        projectFilesGenerator.generateScaffold(tempDir, "Customer", listOf(
                FieldDeclaration("name", FieldDeclaration.FieldType.STRING),
                FieldDeclaration("date_of_birth", FieldDeclaration.FieldType.DATE),
                FieldDeclaration("number_of_dogs", FieldDeclaration.FieldType.INT),
                FieldDeclaration("bed_time", FieldDeclaration.FieldType.DATE_TIME)
        ))

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