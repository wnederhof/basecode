package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.EntityField.RelationalEntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.stereotype.Component

@Component
class RelationalFieldTypeFilter(
    private val nameHelper: NameHelper
) : FieldTemplateFilter {

    override fun enrichProperties(
        fieldIndex: Int,
        settings: FieldTemplateFilter.FieldTemplateSettings
    ): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        if (field !is RelationalEntityField) {
            return listOf()
        }
        val fieldType = field.typeName
        return listOf(
            Variable("fieldTypePascalCase", nameHelper.toUpperCamelCase(fieldType)),
            Variable("fieldTypePluralCamelCase", nameHelper.toLowerCamelCase(nameHelper.pluralize(fieldType))),
            Variable("fieldTypePluralPascalCase", nameHelper.toUpperCamelCase(nameHelper.pluralize(fieldType))),
            Variable("fieldTypeLowerCase", fieldType.toLowerCase()),
            Variable("fieldTypeScreamingSnakeCase", nameHelper.toDuckName(fieldType).toUpperCase()),
            Variable("fieldTypeCamelCase", nameHelper.toLowerCamelCase(fieldType))
        )
    }
}
