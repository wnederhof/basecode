package com.wouter.crudcodegen.generators.impl

import org.springframework.stereotype.Component
import picocli.CommandLine.Command

@Component
@Command(
    name = "fe-scaffold",
    aliases = ["sfe"],
    description = ["Generates frontend scaffold according to the specified entity."],
    mixinStandardHelpOptions = true
)
class FrontendScaffoldGenerator : AbstractFieldBasedGenerator() {
    override val templateNames = listOf("frontend-scaffold")
}
