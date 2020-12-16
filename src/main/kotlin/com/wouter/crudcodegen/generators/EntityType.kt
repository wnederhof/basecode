package com.wouter.crudcodegen.generators

enum class EntityType(val argName: String, val isNullable: Boolean) {
    STRING("string", false),
    INT("int", false),
    TEXT("text", false),
    DATE("date", false),
    DATETIME("datetime", false),
    BOOLEAN("boolean", false),

    NULL_STRING("string_o", true),
    NULL_INT("int_o", true),
    NULL_TEXT("text_o", true),
    NULL_DATE("date_o", true),
    NULL_DATETIME("datetime_o", true),
    NULL_BOOLEAN("boolean_o", true),
}
