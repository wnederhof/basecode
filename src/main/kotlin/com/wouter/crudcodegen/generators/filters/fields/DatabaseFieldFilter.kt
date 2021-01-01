package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.EntityType.*
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import org.springframework.stereotype.Component

@Component
class DatabaseFieldFilter : FieldTemplateFilter {
    override fun enrichProperties(
        fieldIndex: Int,
        settings: FieldTemplateFilter.FieldTemplateSettings
    ): Iterable<Variable> {
        return when (val field = settings.fields[fieldIndex]) {
            is EntityField.RelationalEntityField ->
                listOf(Variable("fieldDatabaseDefinitionType", "INT NOT NULL"))
            is EntityField.PrimitiveEntityField ->
                listOf(Variable("fieldDatabaseDefinitionType", determineDatabaseType(field.entityType)))
        }
    }

    private fun determineDatabaseType(type: EntityType): String {
        return when (type) {
            STRING -> "VARCHAR(255) NOT NULL"
            INT -> "INT NOT NULL"
            TEXT -> "TEXT NOT NULL"
            DATE -> "DATE NOT NULL"
            DATETIME -> "TIMESTAMP NOT NULL"
            BOOLEAN -> "BOOLEAN NOT NULL"

            NULL_STRING -> "VARCHAR(255) NULL"
            NULL_INT -> "INT NULL"
            NULL_TEXT -> "TEXT NULL"
            NULL_DATE -> "DATE NULL"
            NULL_DATETIME -> "TIMESTAMP NULL"
            NULL_BOOLEAN -> "BOOLEAN NULL"
        }
    }
}
