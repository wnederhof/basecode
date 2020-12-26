package com.wouter.crudcodegen.engine

import com.wouter.crudcodegen.Application
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.IOException

@SpringBootTest
internal class FileManagerTest {
    @Autowired
    private lateinit var fileManager: FileManager

    // We mainly test on the "new" template, as it is the least likely template
    // to change structurally.

    @Test
    fun `The contents of the new template pom file contains a group id`() {
        assertThat(fileManager.readTemplate("new", "pom.xml.peb"),
                containsString("<groupId>"))
    }

    @Test
    fun `Temp files and directories can be written directly`() {
        assertThat(fileManager.readTemplate("new", "pom.xml.peb"),
                containsString("<groupId>"))
    }

    @Test
    fun `All files are listed in a template using templateFileTree`() {
        println()
        assertThat(fileManager.listTemplateFilesRecursively("new"),
                hasItems(
                        "pom.xml.peb",
                        "src/main/kotlin/[groupId]/[artifactId]/Application.kt.peb"
                )
        )

        assertThat(fileManager.listTemplateFilesRecursively("new"),
                not(hasItems("src/main/kotlin/[groupId]/[artifactId]")))
    }

    @Test
    fun `Directories can be written even if the target directory does not exist`() {
        val tempDir = createTempDir("crudcodegen_")
        try {
            fileManager.createTargetDirectory(tempDir, "a/b/c")

            assertThat(File(tempDir.path + "/a/b/c").exists(), equalTo(true))
            assertThat(File(tempDir.path + "/a/b/d").exists(), equalTo(false))
            assertThat(File(tempDir.path + "/a/b/c").isDirectory(), equalTo(true))
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun `Files can be written even if the target directory does not exist`() {
        val tempDir = createTempDir("crudcodegen_")
        try {
            fileManager.writeTargetFileContent(tempDir, "a/b/c", "test")

            assertThat(File(tempDir.path + "/a/b/c").exists(), equalTo(true))
            assertThat(File(tempDir.path + "/a/b/d").exists(), equalTo(false))
            assertThat(File(tempDir.path + "/a/b/d").isDirectory(), equalTo(false))
            assertThat(File(tempDir.path + "/a/b/c").readText(), equalTo("test"))
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun `Target files can be read if they exist`() {
        val tempDir = createTempDir("crudcodegen_")
        try {
            fileManager.writeTargetFileContent(tempDir, "a/b/c", "test")

            assertThat(fileManager.readTargetFile(tempDir, "a/b/c"), equalTo("test"))
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun `Target files cannot be read if they dont exist and return null`() {
        val tempDir = createTempDir("crudcodegen_")
        try {
            assertThat(fileManager.readTargetFile(tempDir, "a/b/c"), nullValue())
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun `Directories can be written twice, content stays in place`() {
        val tempDir = createTempDir("crudcodegen_")
        try {
            fileManager.writeTargetFileContent(tempDir, "a/b/c/d", "test")
            fileManager.createTargetDirectory(tempDir, "a/b/c")

            assertThat(File(tempDir.path + "/a/b/c/d").exists(), equalTo(true))
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun `Files can be copied, even when the target directory does not exist`() {
        val tempDir = createTempDir("crudcodegen_")
        try {
            fileManager.copyFile(tempDir, "new", "pom.xml.peb", "a/b/c/d")

            assertThat(File(tempDir.path + "/a/b/c/d").exists(), equalTo(true))
            assertThat(File(tempDir.path + "/a/b/c/d").readText(), containsString("<artifactId>"))
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun `Files cannot be overwritten`() {
        val tempDir = createTempDir("crudcodegen_")
        try {
            fileManager.writeTargetFileContent(tempDir, "a/b/c", "test")
            assertThrows<IOException> {
                fileManager.writeTargetFileContent(tempDir, "a/b/c", "test")
            }
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun `Files can be read and overwritten iff overwrite is true`() {
        val tempDir = createTempDir("crudcodegen_")
        try {
            assertThat(fileManager.readTargetFile(tempDir, "a/b/c"), nullValue())
            fileManager.writeTargetFileContent(tempDir, "a/b/c", "test")
            fileManager.writeTargetFileContent(tempDir, "a/b/c", "test 2", true)
            assertThat(fileManager.readTargetFile(tempDir, "a/b/c"), equalTo("test 2"))
        } finally {
            tempDir.deleteRecursively()
        }
    }
}
