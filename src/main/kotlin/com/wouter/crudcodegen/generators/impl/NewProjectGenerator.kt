package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.generators.Generator
import com.wouter.crudcodegen.generators.GeneratorSettings
import com.wouter.crudcodegen.generators.ProjectProperties
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter
import com.wouter.crudcodegen.generators.helpers.GeneratorSettingsHelper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.File

@Component
@Order(1)
class NewProjectGenerator(
        private val projectTemplateFilters: List<ProjectTemplateFilter>,
        private val generatorSettingsHelper: GeneratorSettingsHelper
) : Generator {

    override fun getSyntax() =
            "new <groupId> <artifactId>"

    override fun getSyntaxDescription() =
            "Generates new project called <artifactId>. For instance: \"new com.wouter example\"."

    override fun acceptsGeneratorName(name: String) = name == "new"

    override fun templateName() = "new"

    override fun initializeGenerator(targetPath: File, properties: ProjectProperties, args: List<String>): GeneratorSettings {
        val (groupId, artifactId) = args

        val updatedProperties = properties.copy(artifactId = artifactId, groupId = groupId)

        return generatorSettingsHelper.generate(targetPath, updatedProperties, args[0], null, projectTemplateFilters)

    }

}
