package com.wouter.crudcodegen.generators

import java.io.File

interface Generator {
    fun getSyntax(): String

    fun acceptsGeneratorName(name: String): Boolean

    fun templateName(): String

    fun initializeGenerator(targetPath: File, properties: ProjectProperties, args: List<String>): GeneratorSettings
}
