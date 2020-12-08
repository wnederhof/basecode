package com.wouter.crudcodegen.generators.impl

import org.springframework.stereotype.Component
import picocli.CommandLine.Command

@Component
@Command(
    name = "fe-scaffold",
    description = ["Generates a frontend scaffold according to the specified entity."],
    mixinStandardHelpOptions = true
)
class FrontendScaffoldGenerator: AbstractFieldBasedGenerator() {
    override val templateName: String = "fe-scaffold"
}
