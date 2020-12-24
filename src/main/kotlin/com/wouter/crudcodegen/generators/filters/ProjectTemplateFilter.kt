package com.wouter.crudcodegen.generators.filters

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.ProjectProperties
import java.io.File

interface ProjectTemplateFilter: GeneratorFilter {
    fun enrichProperties(settings: ProjectTemplateSettings): Iterable<Variable>

    data class ProjectTemplateSettings(
        val groupId: String?,
        val artifactId: String?,
        val theme: ProjectProperties.Theme = ProjectProperties.Theme.plain,
        val name: String,
        val targetPath: File
    )
}
