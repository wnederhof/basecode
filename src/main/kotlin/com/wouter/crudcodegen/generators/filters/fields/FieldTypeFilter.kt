package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.stereotype.Component

@Component
class FieldTypeFilter(
        private val nameHelper: NameHelper
) : FieldTemplateFilter {
    override fun enrichProperties(fieldIndex: Int, settings: FieldTemplateFilter.FieldTemplateSettings): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        return listOf(
                Variable("FieldType", nameHelper.toUpperCamelCase(field.type)),
                Variable("FieldTypes", nameHelper.toUpperCamelCase(nameHelper.pluralize(field.type))),
                Variable("fieldTypes", nameHelper.toLowerCamelCase(nameHelper.pluralize(field.type))),
                Variable("fieldType_lowercase", field.type.toLowerCase()),
                Variable("fieldTypes_lowercase", nameHelper.pluralize(field.type).toLowerCase()),
                Variable("FIELD_TYPE", nameHelper.toDuckName(field.type).toUpperCase()),
                Variable("fieldType", nameHelper.toLowerCamelCase(field.type)),
                Variable("field_types", nameHelper.toLowerCamelCase(nameHelper.pluralize(field.type)))
        )
    }
}
