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
                Variable("fieldTypePascalCase", nameHelper.toUpperCamelCase(field.type)),
                Variable("fieldTypeLowerCase", field.type.toLowerCase()),
                Variable("fieldTypeScreamingSnakeCase", nameHelper.toDuckName(field.type).toUpperCase()),
                Variable("fieldTypeSnakeCase", nameHelper.toLowerCamelCase(field.type))
        )
    }
}
