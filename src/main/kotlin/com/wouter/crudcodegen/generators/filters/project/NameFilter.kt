package com.wouter.crudcodegen.generators.filters.project

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter
import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.stereotype.Component

@Component
class NameFilter(
    private val nameHelper: NameHelper
) : ProjectTemplateFilter {

    override fun enrichProperties(settings: ProjectTemplateFilter.ProjectTemplateSettings): Iterable<Variable> {
        val name = settings.name
        return listOf(
            Variable("nameCamelCase", nameHelper.toLowerCamelCase(name)),
            Variable("namePascalCase", nameHelper.toUpperCamelCase(name)),
            Variable("namePluralCamelCase", nameHelper.pluralize(nameHelper.toLowerCamelCase(name))),
            Variable("nameSnakeCase", nameHelper.toDuckCase(name)),
            Variable("nameLowerCase", name.toLowerCase()),
            Variable("namePluralPascalCase", nameHelper.pluralize(nameHelper.toUpperCamelCase(name))),
            Variable("namePluralSnakeCase", nameHelper.pluralize(nameHelper.toDuckCase(name))),
            Variable("nameKebabCase", nameHelper.toDashCase(name)),
            Variable("namePluralKebabCase", nameHelper.pluralize(nameHelper.toDashCase(name))),
            Variable("nameScreamingSnakeCase", nameHelper.capitalize(nameHelper.toDuckCase(name)))
        )
    }
}
