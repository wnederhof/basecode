package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.stereotype.Component

@Component
class FieldNameFilter(
    private val nameHelper: NameHelper
) : FieldTemplateFilter {
    override fun enrichProperties(
        fieldIndex: Int,
        settings: FieldTemplateFilter.FieldTemplateSettings
    ): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        return listOf(
            Variable("fieldNameSnakeCase", nameHelper.toDuckName(field.name)),
            Variable("fieldNameKebabCase", nameHelper.toDashName(field.name)),
            Variable("fieldNamePascalCase", nameHelper.toUpperCamelCase(field.name)),
            Variable("fieldNameCamelCase", nameHelper.toLowerCamelCase(field.name))
        )
    }
}
