package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.EntityType.*
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import org.springframework.stereotype.Component

@Component
class VueFieldFilter : FieldTemplateFilter {
    override fun enrichProperties(
        fieldIndex: Int,
        settings: FieldTemplateFilter.FieldTemplateSettings
    ): Iterable<Variable> {
        return when (val field = settings.fields[fieldIndex]) {
            is EntityField.RelationalEntityField ->
                listOf(Variable("fieldInputType", "Relationship"))
            is EntityField.PrimitiveEntityField ->
                listOf(Variable("fieldInputType", determineFieldInputType(field.entityType)))
        }
    }

    private fun determineFieldInputType(type: EntityType): String {
        return when (type) {
            STRING -> "String"
            INT -> "Int"
            DATE -> "Date"
            DATETIME -> "DateTime"
            BOOLEAN -> "Boolean"
            TEXT -> "Text"

            NULL_STRING -> "String?"
            NULL_INT -> "Int?"
            NULL_DATE -> "Date?"
            NULL_DATETIME -> "DateTime?"
            NULL_BOOLEAN -> "Boolean?"
            NULL_TEXT -> "Text?"
        }
    }
}
