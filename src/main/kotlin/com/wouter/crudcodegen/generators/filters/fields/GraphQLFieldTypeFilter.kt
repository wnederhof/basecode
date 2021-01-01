package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.EntityType.*
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import org.springframework.stereotype.Component

@Component
class GraphQLFieldTypeFilter : FieldTemplateFilter {
    override fun enrichProperties(
        fieldIndex: Int,
        settings: FieldTemplateFilter.FieldTemplateSettings
    ): Iterable<Variable> {
        return when (val field = settings.fields[fieldIndex]) {
            is EntityField.RelationalEntityField -> listOf(
                Variable("nullableGraphQLFieldType", "Int"),
                Variable("graphQLFieldType", "Int!")
            )
            is EntityField.PrimitiveEntityField -> listOf(
                Variable("nullableGraphQLFieldType", determineNullableGraphQLType(field.entityType)),
                Variable("graphQLFieldType", determineGraphQLType(field.entityType))
            )
        }
    }

    private fun determineNullableGraphQLType(type: EntityType): String {
        return when (type) {
            STRING -> "String"
            INT -> "Int"
            TEXT -> "String"
            DATE -> "Date"
            DATETIME -> "DateTime"
            BOOLEAN -> "Boolean"

            NULL_STRING -> "String"
            NULL_INT -> "Int"
            NULL_TEXT -> "String"
            NULL_DATE -> "Date"
            NULL_DATETIME -> "DateTime"
            NULL_BOOLEAN -> "Boolean"
        }
    }

    private fun determineGraphQLType(type: EntityType): String {
        return when (type) {
            STRING -> "String!"
            INT -> "Int!"
            TEXT -> "String!"
            DATE -> "Date!"
            DATETIME -> "DateTime!"
            BOOLEAN -> "Boolean!"

            NULL_STRING -> "String"
            NULL_INT -> "Int"
            NULL_TEXT -> "String"
            NULL_DATE -> "Date"
            NULL_DATETIME -> "DateTime"
            NULL_BOOLEAN -> "Boolean"
        }
    }
}
