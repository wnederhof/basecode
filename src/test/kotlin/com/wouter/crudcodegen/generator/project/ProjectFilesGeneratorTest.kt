package com.wouter.crudcodegen.generator.project

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.wouter.crudcodegen.generator.engine.MemoryField
import com.wouter.crudcodegen.generator.engine.TemplateEngine
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File

@ExtendWith(MockitoExtension::class)
internal class ProjectFilesGeneratorTest {
    @Mock
    private lateinit var templateEngine: TemplateEngine

    @InjectMocks
    private lateinit var projectFilesGenerator: ProjectFilesGenerator

    @Test
    fun `generateNew generates template using "new"`() {
        val someFile = mock<File>()

        projectFilesGenerator.generateNew(someFile, "com.hello", "world")

        verify(templateEngine).generate(someFile, "new",
                listOf(
                        MemoryField("groupId", "com.hello"),
                        MemoryField("artifactId", "world")
                ))
    }
}