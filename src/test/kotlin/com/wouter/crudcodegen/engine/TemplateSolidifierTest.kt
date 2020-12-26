package com.wouter.crudcodegen.engine

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class TemplateSolidifierTest {
    @Autowired
    private lateinit var templateSolidifier: TemplateSolidifier

    @Test
    fun `Templates can be solidified using a Handlebars template and variables`() {
        val template = "Hello, {{ name }}! How are you {{ moment }}?"
        val variables = listOf(
                Variable("name", "Wouter"),
                Variable("moment", "today")
        )

        val expected = "Hello, Wouter! How are you today?".trimIndent()

        assertThat(templateSolidifier.solidifyTemplate(template, variables),
                equalTo(expected))
    }

    @Test
    fun `Filenames without patterns are taken literally`() {
        assertThat(templateSolidifier.solidifyFilename("test.kt", listOf()),
                equalTo("test.kt"))
    }

    @Test
    fun `Filenames with patterns are replaced`() {
        assertThat(templateSolidifier.solidifyFilename("[name].kt", EXAMPLE_VARIABLES),
                equalTo("blog.kt"))
    }

    @Test
    fun `Dots in folder names are replaced by slashes`() {
        assertThat(templateSolidifier.solidifyFilename("[group]/[artifact]/[name]/[Name].kt", EXAMPLE_VARIABLES),
                equalTo("com/wouter/crudcodegen/blog/Blog.kt"))
    }

    private companion object {
        val EXAMPLE_VARIABLES = listOf(
                Variable("group", "com.wouter"),
                Variable("artifact", "crudcodegen"),
                Variable("name", "blog"),
                Variable("Name", "Blog")
        )
    }
}
