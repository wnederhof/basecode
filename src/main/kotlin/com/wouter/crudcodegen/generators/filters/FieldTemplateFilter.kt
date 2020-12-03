package com.wouter.crudcodegen.generators.filters

import com.wouter.crudcodegen.engine.Variable

interface FieldTemplateFilter: GeneratorFilter {
    fun enrichProperties(fieldIndex: Int, settings: FieldTemplateSettings): Iterable<Variable>

    data class FieldTemplateSettings(
            val groupId: String,
            val artifactId: String,
            val name: String,
            val fields: List<EntityField>
    )
}
