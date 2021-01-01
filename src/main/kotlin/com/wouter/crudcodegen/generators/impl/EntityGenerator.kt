package com.wouter.crudcodegen.generators.impl

import org.springframework.stereotype.Component
import picocli.CommandLine.Command

@Component
@Command(
    name = "entity",
    aliases = ["e"],
    description = ["Generates a new Entity and Migration script according to the specified entity."]
)
class EntityGenerator : AbstractFieldBasedGenerator() {
    override val templateNames = listOf("entity")
    override val generateAfterDeletionTemplateNames = listOf("entity-removal")
}
