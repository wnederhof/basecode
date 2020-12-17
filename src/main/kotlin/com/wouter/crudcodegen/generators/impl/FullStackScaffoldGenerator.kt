package com.wouter.crudcodegen.generators.impl

import org.springframework.stereotype.Component
import picocli.CommandLine.Command

@Component
@Command(
    name = "scaffold",
    aliases = ["s"],
    description = ["Generates front and backend scaffold according to the specified entity."],
    mixinStandardHelpOptions = true
)
class FullStackScaffoldGenerator: AbstractFieldBasedGenerator() {
    override val templateNames = listOf("service", "entity", "graphql", "frontend-scaffold")
}
