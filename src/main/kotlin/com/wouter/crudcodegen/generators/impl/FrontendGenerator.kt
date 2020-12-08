package com.wouter.crudcodegen.generators.impl

import org.springframework.stereotype.Component
import picocli.CommandLine.Command

@Component
@Command(
    name = "frontend",
    description = ["Generates all required files for running the frontend."],
    mixinStandardHelpOptions = true
)
class FrontendGenerator: AbstractFieldBasedGenerator() {
    override val templateName: String = "frontend"
}
