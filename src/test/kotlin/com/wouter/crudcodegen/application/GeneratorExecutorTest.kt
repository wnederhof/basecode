package com.wouter.crudcodegen.application

import com.nhaarman.mockitokotlin2.*
import com.wouter.crudcodegen.engine.TemplateEngine
import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.Generator
import com.wouter.crudcodegen.generators.GeneratorSettings
import com.wouter.crudcodegen.generators.ProjectProperties
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File

@ExtendWith(MockitoExtension::class)
class GeneratorExecutorTest {
    @Mock
    private lateinit var generators: List<Generator>

    @Mock
    private lateinit var templateEngine: TemplateEngine

    @Mock
    private lateinit var projectPropertiesManager: ProjectPropertiesManager

    @InjectMocks
    private lateinit var generatorExecutor: GeneratorExecutor

    @Test
    fun `When execute is called and no generator can be found, return false`() {
        val someGenerator = mock<Generator>()

        whenever(someGenerator.acceptsGeneratorName("some-gen")).thenReturn(false)
        whenever(generators.iterator()).thenReturn(listOf(someGenerator).iterator())

        val actual = generatorExecutor.execute(mock(), "some-gen", listOf())

        assertThat(actual, equalTo(false))

        verify(projectPropertiesManager, times(0)).readProperties(any())
        verify(templateEngine, times(0)).generate(any(), anyString(), any())
        verify(projectPropertiesManager, times(0)).writeProperties(any(), any())
    }

    @Test
    fun `When execute is called and a generator can be found, execute and update properties`() {
        val someGenerator = mock<Generator>()
        val someRootPathFile = mock<File>()
        val someProperties = mock<ProjectProperties>()
        val someUpdatedProperties = mock<ProjectProperties>()
        val someVariables = mock<List<Variable>>()
        val someArgs = mock<List<String>>()

        whenever(someGenerator.acceptsGeneratorName("some-gen")).thenReturn(true)
        whenever(someGenerator.templateName()).thenReturn("some-gen-template")
        whenever(generators.iterator()).thenReturn(listOf(someGenerator).iterator())

        whenever(projectPropertiesManager.readProperties(someRootPathFile))
                .thenReturn(someProperties)

        whenever(someGenerator.initializeGenerator(someRootPathFile, someProperties, someArgs))
                .thenReturn(GeneratorSettings(someUpdatedProperties, someVariables))

        val actual = generatorExecutor.execute(someRootPathFile, "some-gen", someArgs)

        assertThat(actual, equalTo(true))

        verify(templateEngine, times(1))
                .generate(someRootPathFile, "some-gen-template", someVariables)

        verify(projectPropertiesManager, times(1))
                .writeProperties(someRootPathFile, someUpdatedProperties)
    }
}