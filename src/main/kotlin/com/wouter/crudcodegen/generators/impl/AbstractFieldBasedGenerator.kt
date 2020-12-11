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

    @Parameters(
        description = ["<name> (<fieldName>:<fieldType)+ for fieldType string, int, text, date, datetime, boolean, or relation, e.g. Customer."],
        paramLabel = "NAME AND FIELDS",
        arity = "1.."
    )
    open var nameAndFields: List<String>? = null

    abstract val templateNames: List<String>

    override fun run() {
        val args = nameAndFields ?: listOf()
        val name = args[0]
        val targetPath = File(fileManager.currentDir)
        val properties = projectPropertiesManager.readProperties(targetPath)
        val fields = fieldArgsHelper.mapArgsToEntityFields(args.drop(1))
        val filters = entityTemplateFilters + projectTemplateFilters + fieldTemplateFilters

        val variables = variablesHelper.createVariables(targetPath, properties, name, fields, filters)

        templateNames.forEach {
            templateEngine.generate(targetPath, it, variables)
        }
    }

}
