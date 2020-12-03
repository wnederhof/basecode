package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import org.springframework.stereotype.Component

@Component
class GraphQLFieldTypeFilter : FieldTemplateFilter {
    override fun enrichProperties(fieldIndex: Int, settings: FieldTemplateFilter.FieldTemplateSettings): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        return listOf(
                Variable("NullFieldGraphQLType", determineNullableGraphQLType(field.type)),
                Variable("FieldGraphQLType", determineGraphQLType(field.type))
        )
    }

    private fun determineNullableGraphQLType(type: String): String {
        return when (type) {
            "string" -> "String"
            "int" -> "Int"
            "text" -> "String"
            "date" -> "Date"
            "dateTime" -> "Date"
            "string?" -> "String"
            "int?" -> "Int"
            "text?" -> "String"
            "date?" -> "Date"
            "dateTime?" -> "Date"
            else -> "Int"
        }
    }

    private fun determineGraphQLType(type: String): String {
        return when (type) {
            "string" -> "String!"
            "int" -> "Int!"
            "text" -> "String!"
            "date" -> "Date!"
            "datetime" -> "DateTime!"
            "string?" -> "String"
            "int?" -> "Int"
            "text?" -> "String"
            "date?" -> "Date"
            "datetime?" -> "DateTime"
            else -> "Int!"
        }
    }
}
