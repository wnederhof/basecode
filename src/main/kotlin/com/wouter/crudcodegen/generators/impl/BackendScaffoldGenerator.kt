package com.wouter.crudcodegen.generators.impl

import org.springframework.stereotype.Component
import picocli.CommandLine.Command

@Component
@Command(
    name = "be-scaffold",
    description = ["Generates backend scaffold according to the specified entity."],
    mixinStandardHelpOptions = true
)
class BackendScaffoldGenerator: AbstractFieldBasedGenerator() {
    override val templateNames = listOf("service", "entity", "graphql")
}
