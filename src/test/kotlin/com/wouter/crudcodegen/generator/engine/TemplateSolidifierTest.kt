package com.wouter.crudcodegen.generator.engine

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
                MemoryField("name", "Wouter"),
                MemoryField("moment", "today")
        )

        val expected = "Hello, Wouter! How are you today?".trimIndent()

        assertThat(templateSolidifier.generate(template, variables),
                equalTo(expected))
    }
}