package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.EntityType.*
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.stereotype.Component

@Component
class KotlinFieldTypeFilter(
        private val nameHelper: NameHelper
) : FieldTemplateFilter {
    override fun enrichProperties(fieldIndex: Int, settings: FieldTemplateFilter.FieldTemplateSettings): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        val testDummyValueName = nameHelper.toUpperCamelCase(field.name)
        return when (val field = settings.fields[fieldIndex]) {
            is EntityField.RelationalEntityField -> listOf(
                    Variable("fieldKotlinAnnotations", null),
                    Variable("fieldKotlinType", "Int"),
                    Variable("fieldKotlinTypeNotNullable", "Int"),
                    Variable("fieldKotlinTestDummyValue", "10")
            )
            is EntityField.PrimitiveEntityField -> listOf(
                    Variable("fieldKotlinAnnotations", determineKotlinAnnotations(field.entityType)),
                    Variable("fieldKotlinType", determineKotlinTypeNullable(field.entityType)),
                    Variable("fieldKotlinTypeNotNullable", determineKotlinType(field.entityType)),
                    Variable("fieldKotlinTestDummyValue", determineSomeTestValue(testDummyValueName, field.entityType))
            )
        }
    }

    private fun determineKotlinAnnotations(type: EntityType): String? {
        return when (type) {
            STRING -> null
            INT -> null
            TEXT -> "@Lob"
            DATE -> null
            BOOLEAN -> null
            DATETIME -> null

            NULL_STRING -> null
            NULL_INT -> null
            NULL_TEXT -> "@Lob"
            NULL_DATE -> null
            NULL_BOOLEAN -> null
            NULL_DATETIME -> null
        }
    }

    private fun determineKotlinType(type: EntityType): String {
        return when (type) {
            STRING -> "String"
            INT -> "Int"
            TEXT -> "String"
            DATE -> "LocalDate"
            DATETIME -> "LocalDateTime"
            BOOLEAN -> "Boolean"

            NULL_STRING -> "String"
            NULL_INT -> "Int"
            NULL_TEXT -> "String"
            NULL_DATE -> "LocalDate"
            NULL_DATETIME -> "LocalDateTime"
            NULL_BOOLEAN -> "Boolean"
        }
    }

    private fun determineKotlinTypeNullable(type: EntityType): String {
        return when (type) {
            STRING -> "String"
            INT -> "Int"
            TEXT -> "String"
            DATE -> "LocalDate"
            DATETIME -> "LocalDateTime"
            BOOLEAN -> "Boolean"

            NULL_STRING -> "String?"
            NULL_INT -> "Int?"
            NULL_TEXT -> "String?"
            NULL_DATE -> "LocalDate?"
            NULL_DATETIME -> "LocalDateTime?"
            NULL_BOOLEAN -> "Boolean?"
        }
    }

    private fun determineSomeTestValue(name: String, type: EntityType): String {
        return when (type) {
            STRING -> "\"Some $name\""
            INT -> "1"
            TEXT -> "\"Some $name\""
            DATE -> "LocalDate.of(2000, 1, 1)"
            DATETIME -> "LocalDateTime.of(2000, 1, 1, 0, 0)"
            BOOLEAN -> "true"

            NULL_STRING -> "\"Some $name\""
            NULL_INT -> "1"
            NULL_TEXT -> "\"Some $name\""
            NULL_DATE -> "LocalDate.of(2000, 1, 1)"
            NULL_DATETIME -> "LocalDateTime.of(2000, 1, 1, 0, 0)"
            NULL_BOOLEAN -> "true"
        }
    }

}
