package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter.FieldTemplateSettings
import org.springframework.stereotype.Component

@Component
class FieldTypePropertiesFilter : FieldTemplateFilter {
    override fun enrichProperties(fieldIndex: Int, settings: FieldTemplateSettings): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        return listOf(
                Variable("isNullable", field.isNullable),
                Variable("isLast", settings.fields.size - 1 == fieldIndex),
                Variable("isTextInput", field.type == "text" || field.type == "text?"),
                Variable("fieldRelationship" , field.isRelationship)
        )
    }
}
