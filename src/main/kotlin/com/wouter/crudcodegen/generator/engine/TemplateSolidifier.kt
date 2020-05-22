package com.wouter.crudcodegen.generator.engine

import com.github.jknack.handlebars.Handlebars
import org.springframework.stereotype.Service

@Service
class TemplateSolidifier {
    private val handlebars = Handlebars()

    fun generate(template: String, memoryFields: List<MemoryField>): String {
        val variableMap = memoryFields.map { it.name to it.value }.toMap()

        return handlebars.compileInline(template)
                .apply(variableMap)
    }
}