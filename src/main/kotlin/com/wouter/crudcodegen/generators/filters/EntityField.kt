package com.wouter.crudcodegen.generators.filters

data class EntityField(val name: String, val type: String) {
    val isFieldNullable: Boolean = type.endsWith("?")

    val isRelationship = when (type) {
        "string" -> false
        "int" -> false
        "text" -> false
        "date" -> false
        "dateTime" -> false
        "boolean" -> false
        "string?" -> false
        "int?" -> false
        "text?" -> false
        "date?" -> false
        "boolean?" -> false
        "dateTime?" -> false
        else -> true
    }

}
