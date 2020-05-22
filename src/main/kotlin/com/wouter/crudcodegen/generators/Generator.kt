package com.wouter.crudcodegen.generators

interface Generator {
    fun getSyntax(): String

    fun getSyntaxDescription(): String

    fun acceptsGeneratorName(name: String): Boolean

    fun templateName(): String

    fun initializeGenerator(properties: ProjectProperties, args: List<String>): GeneratorSettings
}
