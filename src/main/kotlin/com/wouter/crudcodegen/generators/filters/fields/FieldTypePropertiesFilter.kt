package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.EntityType.NULL_TEXT
import com.wouter.crudcodegen.generators.EntityType.TEXT
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter.FieldTemplateSettings
import org.springframework.stereotype.Component

@Component
class FieldTypePropertiesFilter : FieldTemplateFilter {
    override fun enrichProperties(fieldIndex: Int, settings: FieldTemplateSettings): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        val isFieldOfTypeText = field is EntityField.PrimitiveEntityField
                && (field.entityType == TEXT || field.entityType == NULL_TEXT)
        return listOf(
            Variable("isFieldNullable", field.isFieldNullable),
            Variable("isFieldOfTypeText", isFieldOfTypeText),
            Variable("isFieldRelational", field.isFieldRelational)
        )
    }
}
