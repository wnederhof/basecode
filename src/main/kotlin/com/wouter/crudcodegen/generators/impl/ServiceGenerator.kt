package com.wouter.crudcodegen.generators.impl

import org.springframework.stereotype.Component
import picocli.CommandLine.Command

@Component
@Command(
    name = "service",
    description = ["Generates a Service, Events and Event Listeners according to the specified entity."],
    mixinStandardHelpOptions = true
)
class ServiceGenerator: AbstractFieldBasedGenerator() {
    override val templateName: String = "service"
}
