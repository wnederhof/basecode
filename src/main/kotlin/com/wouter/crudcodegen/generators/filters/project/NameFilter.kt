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
                Variable("nameSnakeCase", nameHelper.toDuckName(name)),
                Variable("nameLowerCase", name.toLowerCase()),
                Variable("namePluralPascalCase", nameHelper.pluralize(nameHelper.toUpperCamelCase(name))),
                Variable("namePluralSnakeCase", nameHelper.pluralize(nameHelper.toDuckName(name))),
                Variable("nameKebabCase", nameHelper.toDashName(name)),
                Variable("namePluralKebabCase", nameHelper.pluralize(nameHelper.toDashName(name))),
                Variable("nameScreamingSnakeCase", nameHelper.capitalize(nameHelper.toDuckName(name)))
        )
    }
}
