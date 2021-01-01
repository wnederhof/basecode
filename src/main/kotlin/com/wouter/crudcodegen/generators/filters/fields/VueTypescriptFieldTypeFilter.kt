package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.EntityType.*
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import org.springframework.stereotype.Component

@Component
class VueTypescriptFieldTypeFilter : FieldTemplateFilter {

    // TODO add tests...

    override fun enrichProperties(
        fieldIndex: Int,
        settings: FieldTemplateFilter.FieldTemplateSettings
    ): Iterable<Variable> {
        return when (val field = settings.fields[fieldIndex]) {
            is EntityField.RelationalEntityField -> listOf(
                Variable("fieldVueTypescriptTestValue", "1")
            )
            is EntityField.PrimitiveEntityField -> listOf(
                Variable(
                    "fieldVueTypescriptTestValue",
                    determineVueTypescriptTestValue(field.name, field.entityType)
                )
            )
        }
    }

    private fun determineVueTypescriptTestValue(name: String, type: EntityType): String {
        return when (type) {
            STRING -> "'Some $name'"
            INT -> "10"
            TEXT -> "'Some $name'"
            DATE -> "'2000-01-01'"
            DATETIME -> "'2000-01-01T00:00'"
            BOOLEAN -> "true"

            NULL_STRING -> "'Some $name'"
            NULL_INT -> "0"
            NULL_TEXT -> "'Some $name'"
            NULL_DATE -> "'2000-01-01'"
            NULL_DATETIME -> "'2000-01-01T00:00'"
            NULL_BOOLEAN -> "true"
        }
    }

}
