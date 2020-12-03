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
                Variable("name", nameHelper.toLowerCamelCase(name)),
                Variable("Name", nameHelper.toUpperCamelCase(name)),
                Variable("names", nameHelper.pluralize(nameHelper.toLowerCamelCase(name))),
                Variable("name_lowercase", name.toLowerCase()),
                Variable("Names", nameHelper.pluralize(nameHelper.toUpperCamelCase(name))),
                Variable("_name", nameHelper.toDuckName(name)),
                Variable("_names", nameHelper.pluralize(nameHelper.toDuckName(name))),
                Variable("name_dash", nameHelper.toDashName(name)),
                Variable("names_dash", nameHelper.pluralize(nameHelper.toDashName(name))),
                Variable("NAME", nameHelper.capitalize(nameHelper.toDuckName(name))),
                Variable("NAMES", nameHelper.capitalize(nameHelper.pluralize(nameHelper.toDuckName(name))))
        )
    }
}
