package com.wouter.crudcodegen.generators

enum class EntityType(val argName: String, val isNullable: Boolean) {
    STRING("string", false),
    INT("int", false),
    TEXT("text", false),
    DATE("date", false),
    DATETIME("dateTime", false),
    BOOLEAN("boolean", false),

    NULL_STRING("string?", true),
    NULL_INT("int?", true),
    NULL_TEXT("text?", true),
    NULL_DATE("date?", true),
    NULL_DATETIME("dateTime?", true),
    NULL_BOOLEAN("boolean?", true),
}
