package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.Generator
import com.wouter.crudcodegen.generators.GeneratorSettings
import com.wouter.crudcodegen.generators.ProjectProperties
import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.File
import java.lang.RuntimeException

@Component
@Order(3)
class ServiceGenerator(
        private val nameHelper: NameHelper
) : Generator {
    override fun getSyntax() =
            "service <name> (<name>:<type>)+"

    override fun getSyntaxDescription() =
            "Generates a new Entity and Migration script. Available types: string, int."

    override fun acceptsGeneratorName(name: String) = name == "service"

    override fun templateName() = "service"

    override fun initializeGenerator(targetPath: File, properties: ProjectProperties, args: List<String>): GeneratorSettings {
        val name = args.first()
        val fields = args.drop(1)

        val groupId = properties.groupId
                ?: error("groupId required but not set.")

        val artifactId = properties.artifactId
                ?: error("artifactId required but not set.")

        return GeneratorSettings(
                updatedProperties = properties,
                variables = listOf(
                        Variable("groupId", groupId),
                        Variable("artifactId", artifactId),
                        Variable("name", nameHelper.toLowerCamelCase(name)),
                        Variable("Name", nameHelper.toUpperCamelCase(name)),
                        Variable("names", nameHelper.pluralize(nameHelper.toLowerCamelCase(name))),
                        Variable("Names", nameHelper.pluralize(nameHelper.toUpperCamelCase(name))),
                        Variable("_name", nameHelper.toDuckName(name)),
                        Variable("_names", nameHelper.pluralize(nameHelper.toDuckName(name))),
                        Variable("NAME", nameHelper.capitalize(name)),
                        Variable("NAMES", nameHelper.capitalize(nameHelper.pluralize(name))),
                        Variable("fields", fields.map {
                            it.split(":").let {
                                Field(
                                        name = nameHelper.toLowerCamelCase(it[0]),
                                        someTestValue = determineSomeTestValue(nameHelper.toUpperCamelCase(it[0]), it[1]),
                                        kotlinAnnotations = determineKotlinAnnotations(it[1])
                                )
                            }
                        }),
                        Variable("nextMigrationPrefix", findNextMigrationPrefix(targetPath))
                ))
    }

    private fun findNextMigrationPrefix(targetPath: File): String {
        val i = findLastMigrationNumber(targetPath) + 1
        return "$i".padStart(4, '0')
    }

    private fun findLastMigrationNumber(targetPath: File): Int {
        val files = File("$targetPath/src/main/resources/db/migration").listFiles()
                ?: return 0

        return files
                .filter { it.endsWith(".sql") }
                .map { it.name }
                .map { findNumberPrefix(it) }
                .maxBy { it } ?: 0
    }

    private fun findNumberPrefix(s: String): Int {
        // TODO test entire class...
        return Integer.parseInt(s.replace(Regex("(\\d+).+"), "\$1"))
    }

    private fun determineSomeTestValue(name: String, type: String): String {
        return when (type) {
            "string" ->
                "\"Some $name\""
            "text" ->
                "\"Some $name\""
            "string?" ->
                "\"Some $name\""
            "text?" ->
                "\"Some $name\""
            else ->
                throw RuntimeException("Unable to determine type $type.")
        }
    }

    private fun determineKotlinAnnotations(type: String): String? {
        return when (type) {
            "string" ->
                null
            "text" ->
                "@Lob"
            "string?" ->
                null
            "text?" ->
                "@Lob"
            else ->
                throw RuntimeException("Unable to determine type $type.")
        }
    }

    private fun determineKotlinType(type: String): String {
        return when (type) {
            "string" ->
                "String"
            "text" ->
                "String"
            "string?" ->
                "String?"
            "text?" ->
                "String?"
            else ->
                throw RuntimeException("Unable to determine type $type.")
        }
    }

    data class Field(
            val name: String,
            val someTestValue: String,
            val kotlinAnnotations: String?
    )
}