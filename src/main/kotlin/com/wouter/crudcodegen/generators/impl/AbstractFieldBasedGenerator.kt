package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.application.ProjectPropertiesManager
import com.wouter.crudcodegen.engine.TemplateEngine
import com.wouter.crudcodegen.generators.filters.EntityTemplateFilter
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter
import com.wouter.crudcodegen.generators.helpers.FieldArgsHelper
import com.wouter.crudcodegen.generators.helpers.VariablesHelper
import org.springframework.beans.factory.annotation.Autowired
import picocli.CommandLine
import picocli.CommandLine.Parameters
import java.io.File

abstract class AbstractFieldBasedGenerator(
) : Runnable {
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
    private lateinit var projectPropertiesManager: ProjectPropertiesManager

    @Parameters(description = ["name and types"])
    private var positionals: List<String>? = null

    abstract val templateName: String

    override fun run() {
        val args = positionals ?: listOf()
        val name = args[0]
        val targetPath = File(System.getProperty("user.dir"))
        val properties = projectPropertiesManager.readProperties(targetPath)
        val fields = fieldArgsHelper.mapArgsToEntityFields(args.drop(1))
        val filters = entityTemplateFilters + projectTemplateFilters + fieldTemplateFilters

        val variables = variablesHelper.createVariables(targetPath, properties, name, fields, filters)

        templateEngine.generate(targetPath, templateName, variables)
    }

}
