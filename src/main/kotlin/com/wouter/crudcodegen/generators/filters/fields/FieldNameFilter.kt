package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.stereotype.Component

@Component
class FieldNameFilter(
        private val nameHelper: NameHelper
): FieldTemplateFilter {
    override fun enrichProperties(fieldIndex: Int, settings: FieldTemplateFilter.FieldTemplateSettings): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        return listOf(
                Variable("field_name", nameHelper.toDuckName(field.name)),
                Variable("field_name_dash", nameHelper.toDashName(field.name)),
                Variable("FieldName", nameHelper.toUpperCamelCase(field.name)),
                Variable("fieldName", nameHelper.toLowerCamelCase(field.name))
        )
    }
}
