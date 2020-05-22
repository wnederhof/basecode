package com.wouter.crudcodegen.generator.project

data class FieldDeclaration(val name: String, val type: FieldType) {
    enum class FieldType {
        STRING,
        INT,
        DATE,
        DATE_TIME
    }
}
