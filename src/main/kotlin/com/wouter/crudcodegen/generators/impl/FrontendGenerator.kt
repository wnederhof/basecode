package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.generators.Generator
import com.wouter.crudcodegen.generators.GeneratorSettings
import com.wouter.crudcodegen.generators.ProjectProperties
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter
import com.wouter.crudcodegen.generators.helpers.VariablesHelper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.File

@Component
@Order(5)
class FrontendGenerator(
        private val projectTemplateFilters: List<ProjectTemplateFilter>,
        private val variablesHelper: VariablesHelper
) : Generator {

    override fun getSyntax() =
            "frontend <artifactId>"

    override fun getSyntaxDescription() =
            "Generates new project called <artifactId>. For instance: \"new example\"."

    override fun acceptsGeneratorName(name: String) = name == "frontend"

    override fun templateName() = "frontend"

    override fun initializeGenerator(targetPath: File, properties: ProjectProperties, args: List<String>): GeneratorSettings {
        return GeneratorSettings(
                updatedProperties = properties,
                variables = variablesHelper.createVariables(targetPath, properties, args[0], null, projectTemplateFilters)
        )
    }

}
