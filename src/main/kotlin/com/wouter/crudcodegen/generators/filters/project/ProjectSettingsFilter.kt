package com.wouter.crudcodegen.generators.filters.project

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.ProjectProperties
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter
import org.springframework.stereotype.Component

@Component
class ProjectSettingsFilter : ProjectTemplateFilter {
    override fun enrichProperties(settings: ProjectTemplateFilter.ProjectTemplateSettings): Iterable<Variable> {
        return listOf(
            Variable("groupId", settings.groupId),
            Variable("artifactId", settings.artifactId),
            Variable("usesBootstrapTheme", settings.theme == ProjectProperties.Theme.bootstrap)
        )
    }
}
