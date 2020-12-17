package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.application.ProjectPropertiesManager
import com.wouter.crudcodegen.engine.FileManager
import com.wouter.crudcodegen.engine.TemplateEngine
import com.wouter.crudcodegen.generators.filters.EntityTemplateFilter
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter
import com.wouter.crudcodegen.generators.helpers.FieldArgsHelper
import com.wouter.crudcodegen.generators.helpers.VariablesHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import picocli.CommandLine
import picocli.CommandLine.Command
import java.io.File

@Component
@Command(
    name = "frontend-support",
    aliases = ["fes"],
    description = ["Generates all required files for running the frontend."],
    mixinStandardHelpOptions = true
)
class FrontendSupportGenerator(
    private val projectTemplateFilters: List<ProjectTemplateFilter>,
    private val fieldArgsHelper: FieldArgsHelper,
    private val variablesHelper: VariablesHelper,
    private val templateEngine: TemplateEngine,
    private val projectPropertiesManager: ProjectPropertiesManager,
    private val fileManager: FileManager
) : Runnable {

    override fun run() {
        val targetPath = File(fileManager.currentDir)
        val properties = projectPropertiesManager.readProperties(targetPath)
        val fields = fieldArgsHelper.mapArgsToEntityFields(listOf())
        val filters = projectTemplateFilters

        val variables = variablesHelper.createVariables(targetPath, properties, "", fields, filters)

        templateEngine.generate(targetPath, "frontend", variables)
    }

}
