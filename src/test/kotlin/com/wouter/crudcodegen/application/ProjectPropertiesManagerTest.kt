package com.wouter.crudcodegen.application

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wouter.crudcodegen.application.ProjectPropertiesManager.Companion.PROPERTIES_FILE_NAME
import com.wouter.crudcodegen.engine.FileManager
import com.wouter.crudcodegen.generators.ProjectProperties
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File

@ExtendWith(MockitoExtension::class)
internal class ProjectPropertiesManagerTest {
    @Mock
    private lateinit var fileManager: FileManager
    @InjectMocks
    private lateinit var projectPropertiesManager: ProjectPropertiesManager

    @Test
    fun `readProperties reads properties from property file`() {
        val examplePropsFile = """
            ---
            groupId: "test 2"
            artifactId: "test"
        """.trimIndent()

        val contextPath = mock<File>()

        whenever(fileManager.readTargetFile(contextPath, PROPERTIES_FILE_NAME))
                .thenReturn(examplePropsFile)

        fileManager.readTargetFile(contextPath, PROPERTIES_FILE_NAME)

        val expected = ProjectProperties(groupId = "test 2", artifactId = "test")

        assertThat(projectPropertiesManager.readProperties(contextPath), equalTo(expected))
    }

    @Test
    fun `readProperties returns empty Props file if it doesnt exist`() {
        val contextPath = mock<File>()

        whenever(fileManager.readTargetFile(contextPath, PROPERTIES_FILE_NAME))
                .thenReturn(null)

        fileManager.readTargetFile(contextPath, PROPERTIES_FILE_NAME)

        val expected = ProjectProperties()

        assertThat(projectPropertiesManager.readProperties(contextPath), equalTo(expected))
    }

    @Test
    fun `writeProperties writes properties to property file`() {
        val properties = ProjectProperties(groupId = "test 2", artifactId = "test")
        val contextPath = mock<File>()

        fileManager.readTargetFile(contextPath, PROPERTIES_FILE_NAME)

        projectPropertiesManager.writeProperties(contextPath, properties)

        val expectedPropsFile = """
            ---
            groupId: "test 2"
            artifactId: "test"
        """.trimIndent()

        verify(fileManager).writeTargetFileContent(contextPath, PROPERTIES_FILE_NAME, "$expectedPropsFile\n", true)
    }
}