package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.stereotype.Component

@Component
class KotlinFieldTypeFilter(private val nameHelper: NameHelper) : FieldTemplateFilter {
    override fun enrichProperties(fieldIndex: Int, settings: FieldTemplateFilter.FieldTemplateSettings): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        return listOf(
                Variable("fieldKotlinAnnotations", determineKotlinAnnotations(field.type)),
                Variable("fieldKotlinType", determineKotlinTypeNullable(field.type)),
                Variable("fieldKotlinTypeNotNullable", determineKotlinType(field.type)),
                Variable("fieldKotlinTestDummyValue", determineSomeTestValue(nameHelper.toUpperCamelCase(field.name), field.type))
        )
    }

    private fun determineKotlinAnnotations(type: String): String? {
        return when (type) {
            "string" -> null
            "int" -> null
            "text" -> "@Lob"
            "date" -> null
            "boolean" -> null
            "dateTime" -> null
            "string?" -> null
            "int?" -> null
            "text?" -> "@Lob"
            "date?" -> null
            "boolean?" -> null
            "dateTime?" -> null
            else -> null
        }
    }

    private fun determineKotlinType(type: String): String {
        return when (type) {
            "string" -> "String"
            "int" -> "Int"
            "text" -> "String"
            "date" -> "LocalDate"
            "dateTime" -> "LocalDateTime"
            "boolean" -> "Boolean"
            "string?" -> "String"
            "int?" -> "Int"
            "text?" -> "String"
            "date?" -> "LocalDate"
            "dateTime?" -> "LocalDateTime"
            "boolean?" -> "Boolean"
            else -> "Int"
        }
    }

    private fun determineKotlinTypeNullable(type: String): String {
        return when (type) {
            "string" -> "String"
            "int" -> "Int"
            "text" -> "String"
            "date" -> "LocalDate"
            "dateTime" -> "LocalDateTime"
            "boolean" -> "Boolean"
            "string?" -> "String?"
            "int?" -> "Int?"
            "text?" -> "String?"
            "date?" -> "LocalDate?"
            "dateTime?" -> "LocalDateTime?"
            "boolean?" -> "Boolean?"
            else -> "Int"
        }
    }

    private fun determineSomeTestValue(name: String, type: String): String {
        return when (type) {
            "string" -> "\"Some $name\""
            "int" -> "1"
            "text" -> "\"Some $name\""
            "date" -> "LocalDate.of(2000, 1, 1)"
            "dateTime" -> "LocalDateTime.of(2000, 1, 1, 0, 0)"
            "boolean" -> "true"
            "string?" -> "\"Some $name\""
            "int?" -> "1"
            "text?" -> "\"Some $name\""
            "date?" -> "LocalDate.of(2000, 1, 1)"
            "dateTime?" -> "LocalDateTime.of(2000, 1, 1, 0, 0)"
            "boolean?" -> "true"
            else -> "10"
        }
    }


}
