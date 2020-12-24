package com.wouter.crudcodegen.generators.filters

import com.wouter.crudcodegen.engine.Variable

interface EntityTemplateFilter: GeneratorFilter {
    fun enrichProperties(settings: EntityTemplateSettings): Iterable<Variable>

    data class EntityTemplateSettings(
            val fields: List<EntityField>
    )
}
