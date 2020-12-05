package com.wouter.crudcodegen.generators.filters.project

import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter.ProjectTemplateSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.nio.file.Files

@SpringBootTest
class DatabaseFilterTest {
    @Autowired
    private lateinit var databaseFilter: DatabaseFilter

    private lateinit var tempDir: File

    @BeforeEach
    fun setup() {
        tempDir = Files.createTempDirectory("crudcodegen_").toFile()
    }

    @AfterEach
    fun teardown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `enrichProperties checks which files are in the migration path, sets nextMigrationPrefix appropriately`() {
        val migrationDir = File(tempDir.path + "/src/main/resources/db/migration")
        val migrationFile = File(tempDir.path + "/src/main/resources/db/migration/V001__some-migration.sql")

        migrationDir.mkdirs()
        migrationFile.createNewFile()

        val settings = ProjectTemplateSettings(
                groupId = SOME_STRING,
                artifactId = SOME_STRING,
                name = SOME_STRING,
                targetPath = tempDir
        )
        val actual = databaseFilter.enrichProperties(settings)

        assertThat(actual.single().name).isEqualTo("nextMigrationPrefix")
        assertThat(actual.single().value).isEqualTo("002")
    }

    private companion object {
        const val SOME_STRING = "some-irrelevant-string"
    }
}
