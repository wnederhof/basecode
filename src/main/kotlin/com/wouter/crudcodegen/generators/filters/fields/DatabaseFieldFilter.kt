package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import org.springframework.stereotype.Component

@Component
class DatabaseFieldFilter : FieldTemplateFilter {
    override fun enrichProperties(fieldIndex: Int, settings: FieldTemplateFilter.FieldTemplateSettings): Iterable<Variable> {
        val field = settings.fields[fieldIndex]
        return listOf(Variable("FIELD_DATABASE_TYPE", determineDatabaseType(field.type)))
    }

    private fun determineDatabaseType(type: String): String {
        return when (type) {
            "string" -> "VARCHAR(255) NOT NULL"
            "int" -> "INT NOT NULL"
            "text" -> "TEXT NOT NULL"
            "date" -> "DATE NOT NULL"
            "dateTime" -> "TIMESTAMP NOT NULL"
            "boolean" -> "BOOLEAN NOT NULL"
            "string?" -> "VARCHAR(255) NULL"
            "int?" -> "INT NULL"
            "text?" -> "TEXT NULL"
            "date?" -> "DATE NULL"
            "dateTime?" -> "TIMESTAMP NULL"
            "boolean?" -> "BOOLEAN NULL"
            else -> "INT NOT NULL"
        }
    }

}
