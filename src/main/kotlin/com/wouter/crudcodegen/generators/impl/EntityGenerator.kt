package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.generators.Generator
import com.wouter.crudcodegen.generators.GeneratorSettings
import com.wouter.crudcodegen.generators.ProjectProperties
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.EntityTemplateFilter
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter
import com.wouter.crudcodegen.generators.helpers.FieldArgsHelper
import com.wouter.crudcodegen.generators.helpers.GeneratorSettingsHelper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.File

@Component
@Order(2)
class EntityGenerator(
        private val entityTemplateFilters: List<EntityTemplateFilter>,
        private val projectTemplateFilters: List<ProjectTemplateFilter>,
        private val fieldTemplateFilters: List<FieldTemplateFilter>,
        private val fieldArgsHelper: FieldArgsHelper,
        private val generatorSettingsHelper: GeneratorSettingsHelper
) : Generator {

    override fun getSyntax() =
            "entity <name> (<name>:<type>)+"

    override fun getSyntaxDescription() =
            "Generates a new Entity and Migration script. Available types: string, int."

    override fun acceptsGeneratorName(name: String) = name == "entity"

    override fun templateName() = "entity"

    override fun initializeGenerator(targetPath: File, properties: ProjectProperties, args: List<String>): GeneratorSettings {
        val fields = fieldArgsHelper.mapArgsToEntityFields(args.drop(1))
        val filters = entityTemplateFilters + projectTemplateFilters + fieldTemplateFilters
        return generatorSettingsHelper.generate(targetPath, properties, args[0], fields, filters)
    }

}
