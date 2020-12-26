package com.wouter.crudcodegen.engine

import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File

@ExtendWith(MockitoExtension::class)
internal class TemplateEngineTest {
    @Mock
    private lateinit var fileManager: FileManager

    @Mock
    private lateinit var templateSolidifier: TemplateSolidifier

    @InjectMocks
    private lateinit var templateEngine: TemplateEngine

    @Test
    fun `File and filenames are generated from template specs`() {
        val root = mock<File>()
        val someMemoryFields = listOf(
                Variable("artifact", "helloworld"),
                Variable("pomFileName", "pom")
        )

        whenever(fileManager.listTemplateFilesRecursively("new"))
                .thenReturn(listOf(
                        "[pomFileName].xml.peb",
                        "README.md"
                ))

        doReturn("pom.xml.peb")
                .whenever(templateSolidifier).solidifyFilename("[pomFileName].xml.peb", someMemoryFields)

        doReturn("README.md")
                .whenever(templateSolidifier).solidifyFilename("README.md", someMemoryFields)

        doReturn("""<artifact>{{ artifact }}</artifact>""")
                .whenever(fileManager).readTemplate("new", "[pomFileName].xml.peb")

        whenever(templateSolidifier.solidifyTemplate("""<artifact>{{ artifact }}</artifact>""", someMemoryFields))
                .thenReturn("<artifact>helloworld</artifact>")

        templateEngine.generate(root, "new", someMemoryFields)

        verify(fileManager, times(1))
                .copyFile(root, "new", "README.md", "README.md")

        verify(fileManager, times(1))
                .writeTargetFileContent(root, "pom.xml", "<artifact>helloworld</artifact>")
    }
}
