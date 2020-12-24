package com.wouter.crudcodegen.generators.helpers

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.GeneratorSettings
import com.wouter.crudcodegen.generators.ProjectProperties
import com.wouter.crudcodegen.generators.filters.*
import com.wouter.crudcodegen.generators.filters.EntityTemplateFilter.EntityTemplateSettings
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter.ProjectTemplateSettings
import org.springframework.stereotype.Service
import java.io.File

@Service
class VariablesHelper {

    fun createVariables(
        targetPath: File,
        properties: ProjectProperties,
        name: String,
        fields: List<EntityField>? = null,
        filters: List<GeneratorFilter>
    ): List<Variable> {
        val groupId = properties.groupId ?: error("groupId required but not set.")
        val artifactId = properties.artifactId ?: error("artifactId required but not set.")
        val theme = properties.theme
        val variables = mutableListOf<Variable>()
        val projectTemplateFilters = filters.filterIsInstance(ProjectTemplateFilter::class.java)
        val entityTemplateFilters = filters.filterIsInstance(EntityTemplateFilter::class.java)
        val fieldTemplateFilters = filters.filterIsInstance(FieldTemplateFilter::class.java)

        variables += projectTemplateFilters.flatMap {
            it.enrichProperties(ProjectTemplateSettings(groupId, artifactId, theme, name, targetPath))
        }

        if (fields != null) {
            variables += entityTemplateFilters.flatMap {
                it.enrichProperties(EntityTemplateSettings(fields))
            }

            variables += Variable("fields", fields.mapIndexed { idx, _ ->
                fieldTemplateFilters.flatMap {
                    it.enrichProperties(
                        idx,
                        FieldTemplateFilter.FieldTemplateSettings(fields)
                    )
                }.map { it.name to it.value }.toMap()
            })
        }
        return variables
    }

}
