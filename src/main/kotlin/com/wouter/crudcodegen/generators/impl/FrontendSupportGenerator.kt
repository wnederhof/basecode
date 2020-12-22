package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.application.ProjectPropertiesManager
import com.wouter.crudcodegen.engine.FileManager
import com.wouter.crudcodegen.engine.TemplateEngine
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter
import com.wouter.crudcodegen.generators.helpers.FieldArgsHelper
import com.wouter.crudcodegen.generators.helpers.VariablesHelper
import org.springframework.stereotype.Component
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File

@Component
@Command(
    name = "frontend-support",
    aliases = ["fes"],
    description = ["Generates all required files for running the frontend."]
)
class FrontendSupportGenerator(
    private val projectTemplateFilters: List<ProjectTemplateFilter>,
    private val fieldArgsHelper: FieldArgsHelper,
    private val variablesHelper: VariablesHelper,
    private val templateEngine: TemplateEngine,
    private val projectPropertiesManager: ProjectPropertiesManager,
    private val fileManager: FileManager
) : Runnable {
    @Option(names = ["-d", "--delete"])
    var deleteFlag: Boolean = false

    @Option(names = ["-o", "--overwrite"])
    var overwriteFlag: Boolean = false

    @Option(usageHelp = true, names = ["-h", "--help"])
    var usageHelp: Boolean = false

    override fun run() {
        val targetPath = File(fileManager.currentDir)
        val properties = projectPropertiesManager.readProperties(targetPath)
        val fields = fieldArgsHelper.mapArgsToEntityFields(listOf())
        val filters = projectTemplateFilters

        val variables = variablesHelper.createVariables(targetPath, properties, "", fields, filters)

        val alreadyExistingTemplateFile = templateEngine.findAlreadyExistingTargetFiles(targetPath, "frontend", variables)
            .firstOrNull()

        if (alreadyExistingTemplateFile != null && !overwriteFlag && !deleteFlag) {
            error("Aborting. File already exists: $alreadyExistingTemplateFile. If you wish to overwrite existing files, please use -o or --overwrite.")
        }

        if (deleteFlag) {
            templateEngine.delete(targetPath, "frontend", variables)
        } else {
            templateEngine.generate(targetPath, "frontend", variables)
        }
    }

}
