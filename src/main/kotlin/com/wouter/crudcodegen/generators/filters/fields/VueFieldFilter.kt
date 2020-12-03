package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import org.springframework.stereotype.Component

@Component
class VueFieldFilter : FieldTemplateFilter {
    override fun enrichProperties(fieldIndex: Int, settings: FieldTemplateFilter.FieldTemplateSettings): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        return listOf(Variable("fieldHtmlInputType", determineFieldInputType(field.type)))
    }

    private fun determineFieldInputType(type: String): String {
        return when (type) {
            "string" -> "text"
            "int" -> "number"
            "date" -> "date"
            "dateTime" -> "datetime-local"
            "boolean" -> "checkbox"
            "string?" -> "text"
            "int?" -> "number"
            "date?" -> "date"
            "dateTime?" -> "datetime-local"
            "boolean?" -> "checkbox"
            else -> "text"
        }
    }
}
