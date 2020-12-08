package com.wouter.crudcodegen.application

import com.wouter.crudcodegen.application.CommandLineInterface.GenerateCommand
import com.wouter.crudcodegen.application.CommandLineInterface.NewCommand
import com.wouter.crudcodegen.engine.TemplateEngine
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
        private val projectPropertiesManager: ProjectPropertiesManager
    ) : Runnable {

        @Option(usageHelp = true, names = ["-h", "--help"])
        var usageHelp: Boolean = false

        @Option(names = ["-be", "--backend-only"])
        var backendOnly: Boolean = false

        @Parameters(description = ["groupId, e.g. com.petparadise"], index = "0")
        private var groupId: String? = null

        @Parameters(description = ["artifactId, e.g. petstore"], index = "1")
        private var artifactId: String? = null

        override fun run() {
            val targetPath = File(System.getProperty("user.dir") + "/$artifactId")
            targetPath.mkdirs()
            val properties = projectPropertiesManager.readProperties(targetPath)
                .copy(artifactId = artifactId!!, groupId = groupId!!)
            val filters = entityTemplateFilters + projectTemplateFilters + fieldTemplateFilters

            val variables = variablesHelper.createVariables(targetPath, properties, artifactId!!, null, filters)

            templateEngine.generate(targetPath, "new", variables)
            if (!backendOnly) {
                templateEngine.generate(targetPath, "frontend", variables)
            }

            projectPropertiesManager.writeProperties(targetPath, properties)
        }
    }
}
