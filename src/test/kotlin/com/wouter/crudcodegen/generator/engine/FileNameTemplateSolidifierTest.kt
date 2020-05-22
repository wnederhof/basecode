package com.wouter.crudcodegen.generator.engine

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class FileNameTemplateSolidifierTest {
    @InjectMocks
    private lateinit var fileNameTemplateSolidifier: FileNameTemplateSolidifier

    @Test
    fun `Filenames without patterns are taken literally`() {
        assertThat(fileNameTemplateSolidifier.generate("test.kt", listOf()),
                equalTo("test.kt"))
    }

    @Test
    fun `Filenames with patterns are replaced`() {
        assertThat(fileNameTemplateSolidifier.generate("[name].kt", EXAMPLE_VARIABLES),
                equalTo("blog.kt"))
    }

    @Test
    fun `Dots in folder names are replaced by slashes`() {
        assertThat(fileNameTemplateSolidifier.generate("[group]/[artifact]/[name]/[Name].kt", EXAMPLE_VARIABLES),
                equalTo("com/wouter/crudcodegen/blog/Blog.kt"))
    }

    private companion object {
        val EXAMPLE_VARIABLES = listOf(
                MemoryField("group", "com.wouter"),
                MemoryField("artifact", "crudcodegen"),
                MemoryField("name", "blog"),
                MemoryField("Name", "Blog")
        )
    }
}