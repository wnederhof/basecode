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
                listOf(Variable("fieldHtmlInputType", "number"))
            is EntityField.PrimitiveEntityField ->
                listOf(Variable("fieldHtmlInputType", determineFieldInputType(field.entityType)))
        }
    }

    private fun determineFieldInputType(type: EntityType): String {
        return when (type) {
            STRING -> "text"
            INT -> "number"
            DATE -> "date"
            DATETIME -> "datetime-local"
            BOOLEAN -> "checkbox"
            TEXT -> "text"

            NULL_STRING -> "text"
            NULL_INT -> "number"
            NULL_DATE -> "date"
            NULL_DATETIME -> "datetime-local"
            NULL_BOOLEAN -> "checkbox"
            NULL_TEXT -> "text"
        }
    }
}
