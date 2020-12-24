package com.wouter.crudcodegen.application

import com.wouter.crudcodegen.application.CommandLineInterface.GenerateCommand
import com.wouter.crudcodegen.application.CommandLineInterface.NewCommand
import com.wouter.crudcodegen.engine.FileManager
import com.wouter.crudcodegen.engine.TemplateEngine
import com.wouter.crudcodegen.generators.ProjectProperties
import com.wouter.crudcodegen.generators.filters.EntityTemplateFilter
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter
import com.wouter.crudcodegen.generators.helpers.VariablesHelper
import com.wouter.crudcodegen.generators.impl.*
import org.springframework.stereotype.Component
import picocli.CommandLine.*
import java.io.File

@Component
@Command(
    name = "ccg",
    header = ["CrudCodeGen - Open Source Full Stack Code Generator", ""],
    subcommands = [GenerateCommand::class, NewCommand::class]
)
class CommandLineInterface {

    @Option(usageHelp = true, names = ["-h", "--help"])
    var usageHelp: Boolean = false

    @Component
    @Command(
        name = "generate",
        aliases = ["g"],
        subcommands = [
            FullStackScaffoldGenerator::class,
            BackendScaffoldGenerator::class,
            FrontendScaffoldGenerator::class,
            EntityGenerator::class,
            GraphQLGenerator::class,
            ServiceGenerator::class,
            FrontendSupportGenerator::class
        ]
    )
    class GenerateCommand {

        @Option(usageHelp = true, names = ["-h", "--help"])
        var usageHelp: Boolean = false

    }

    @Component
    @Command(
        name = "new",
        aliases = ["n"]
    )
    class NewCommand(
        private val entityTemplateFilters: List<EntityTemplateFilter>,
        private val projectTemplateFilters: List<ProjectTemplateFilter>,
        private val fieldTemplateFilters: List<FieldTemplateFilter>,
        private val variablesHelper: VariablesHelper,
        private val templateEngine: TemplateEngine,
        private val fileManager: FileManager,
        private val projectPropertiesManager: ProjectPropertiesManager
    ) : Runnable {

        @Option(names = ["-o", "--overwrite"])
        var overwriteFlag: Boolean = false

        @Option(usageHelp = true, names = ["-h", "--help"])
        var usageHelp: Boolean = false

        @Option(names = ["-t", "--theme"])
        var theme: ProjectProperties.Theme? = null

        @Option(names = ["-b", "--backend-only"])
        var backendOnly: Boolean = false

        @Parameters(description = ["groupId, e.g. com.petparadise"], index = "0")
        private var groupId: String? = null

        @Parameters(description = ["artifactId, e.g. petstore"], index = "1")
        private var artifactId: String? = null

        override fun run() {
            val targetPath = File(fileManager.currentDir + "/$artifactId")
            targetPath.mkdirs()
            val properties = projectPropertiesManager.readProperties(targetPath)
                .copy(artifactId = artifactId!!, groupId = groupId!!, theme = theme ?: ProjectProperties.Theme.plain)
            val filters = entityTemplateFilters + projectTemplateFilters + fieldTemplateFilters

            val variables = variablesHelper.createVariables(targetPath, properties, artifactId!!, null, filters)

            val templateNames = if (backendOnly) listOf("new") else listOf("new", "frontend")

            val alreadyExistingTemplateFile = templateNames
                .flatMap { templateEngine.findAlreadyExistingTargetFiles(targetPath, it, variables) }
                .firstOrNull()

            if (alreadyExistingTemplateFile != null && !overwriteFlag) {
                error("Aborting. File already exists: $alreadyExistingTemplateFile. If you wish to overwrite existing files, please use -o or --overwrite.")
            }

            templateNames.forEach {
                templateEngine.generate(targetPath, it, variables)
            }

            projectPropertiesManager.writeProperties(targetPath, properties)
        }
    }
}
