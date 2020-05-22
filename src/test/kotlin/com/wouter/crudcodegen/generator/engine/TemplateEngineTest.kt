package com.wouter.crudcodegen.generator.engine

import com.nhaarman.mockitokotlin2.*
import com.wouter.crudcodegen.generator.io.FileManager
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

    @Mock
    private lateinit var fileNameTemplateSolidifier: FileNameTemplateSolidifier

    @InjectMocks
    private lateinit var templateEngine: TemplateEngine

    @Test
    fun `File and filenames are generated from template specs`() {
        val root = mock<File>()
        val someMemoryFields = listOf(
                MemoryField("artifact", "helloworld"),
                MemoryField("pomFileName", "pom")
        )

        whenever(fileManager.listTemplateFilesRecursively("new"))
                .thenReturn(listOf(
                        "[pomFileName].xml.hbs",
                        "README.md"
                ))

        doReturn("pom.xml.hbs")
                .whenever(fileNameTemplateSolidifier).generate("[pomFileName].xml.hbs", someMemoryFields)

        doReturn("README.md")
                .whenever(fileNameTemplateSolidifier).generate("README.md", someMemoryFields)

        doReturn("""<artifact>{{ artifact }}</artifact>""")
                .whenever(fileManager).readTemplate("new", "[pomFileName].xml.hbs")

        whenever(templateSolidifier.generate("""<artifact>{{ artifact }}</artifact>""", someMemoryFields))
                .thenReturn("<artifact>helloworld</artifact>")

        templateEngine.generate(root, "new", someMemoryFields)

        verify(fileManager, times(1))
                .copyFile(root, "new", "README.md", "README.md")

        verify(fileManager, times(1))
                .writeTargetFileContent(root, "pom.xml", "<artifact>helloworld</artifact>")
    }
}