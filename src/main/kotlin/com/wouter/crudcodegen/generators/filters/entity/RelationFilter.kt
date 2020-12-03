package com.wouter.crudcodegen.generators.filters.entity

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.EntityTemplateFilter
import org.springframework.stereotype.Component

@Component
class RelationFilter : EntityTemplateFilter {
    override fun enrichProperties(settings: EntityTemplateFilter.EntityTemplateSettings): Iterable<Variable> {
        val fields = settings.fields
        return listOf(Variable("hasRelations", fields.any { it.isFieldRelational }))
    }
}
