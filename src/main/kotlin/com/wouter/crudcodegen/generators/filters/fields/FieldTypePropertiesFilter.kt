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
                Variable("isFieldNullable", field.isFieldNullable),
                Variable("isFieldOfTypeText", field.type == "text" || field.type == "text?"),
                Variable("isFieldRelational" , field.isRelationship)
        )
    }
}
