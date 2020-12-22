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
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import java.io.File

abstract class AbstractFieldBasedGenerator : Runnable {
    @Autowired
    private lateinit var entityTemplateFilters: List<EntityTemplateFilter>

    @Autowired
    private lateinit var projectTemplateFilters: List<ProjectTemplateFilter>

    @Autowired
    private lateinit var fieldTemplateFilters: List<FieldTemplateFilter>

    @Autowired
    private lateinit var fieldArgsHelper: FieldArgsHelper

    @Autowired
    private lateinit var variablesHelper: VariablesHelper

    @Autowired
    private lateinit var templateEngine: TemplateEngine

    @Autowired
    private lateinit var fileManager: FileManager

    @Autowired
    private lateinit var projectPropertiesManager: ProjectPropertiesManager

    @Option(names = ["-d", "--delete"])
    var deleteFlag: Boolean = false

    @Option(names = ["-o", "--overwrite"])
    var overwriteFlag: Boolean = false

    @Option(usageHelp = true, names = ["-h", "--help"])
    var usageHelp: Boolean = false

    @Parameters(
        description = ["<name> (<fieldName>:<fieldType)+ for fieldType string, int, text, date, datetime, boolean, " +
                "or relation, e.g. Customer. Add _o for optional, e.g. string_o."],
        paramLabel = "NAME AND FIELDS",
        arity = "1.."
    )
    open var nameAndFields: List<String>? = null

    abstract val templateNames: List<String>

    open val generateAfterDeletionTemplateNames: List<String> = listOf()

    override fun run() {
        val args = nameAndFields ?: listOf()
        val name = args[0]
        val targetPath = File(fileManager.currentDir)
        val properties = projectPropertiesManager.readProperties(targetPath)
        val fields = fieldArgsHelper.mapArgsToEntityFields(args.drop(1))
        val filters = entityTemplateFilters + projectTemplateFilters + fieldTemplateFilters

        val variables = variablesHelper.createVariables(targetPath, properties, name, fields, filters)

        val alreadyExistingTemplateFile = templateNames
            .flatMap { templateEngine.findAlreadyExistingTargetFiles(targetPath, it, variables) }
            .firstOrNull()

        if (alreadyExistingTemplateFile != null && !overwriteFlag && !deleteFlag) {
            error("Aborting. File already exists: $alreadyExistingTemplateFile. If you wish to overwrite existing files, please use -o or --overwrite.")
        }

        templateNames.forEach {
            if (deleteFlag) {
                templateEngine.delete(targetPath, it, variables)
            } else {
                templateEngine.generate(targetPath, it, variables)
            }
        }

        if (deleteFlag) {
            generateAfterDeletionTemplateNames.forEach {
                templateEngine.generate(targetPath, it, variables)
            }
        }
    }

}
