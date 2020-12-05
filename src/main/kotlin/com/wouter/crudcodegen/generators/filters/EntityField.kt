package com.wouter.crudcodegen.generators.filters

import com.wouter.crudcodegen.generators.EntityType

sealed class EntityField(
        val name: String,
        val isFieldNullable: Boolean,
        val isFieldRelational: Boolean
) {

    data class PrimitiveEntityField(private val _name: String, val entityType: EntityType)
        : EntityField(_name, entityType.isNullable, false)

    data class RelationalEntityField(private val _name: String, val typeName: String)
        : EntityField(_name, false, true)

    companion object {
        fun fromUserInput(name: String, type: String): EntityField {
            if (EntityType.values().any { it.argName == type }) {
                val entityType = EntityType.values().single { it.argName == type }
                return PrimitiveEntityField(name, entityType)
            }

            if (!type.first().isUpperCase()) {
                error("If the type is not a primitive, it must be an entity (which always starts with an uppercase.)")
            }
            return RelationalEntityField(name, type)
        }
    }
}
