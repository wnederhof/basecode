package com.wouter.crudcodegen.generators.impl

import org.springframework.stereotype.Component
import picocli.CommandLine.Command

@Component
@Command(
    name = "service",
    aliases = ["srv"],
    description = ["Generates a Service, Events and Event Listeners according to the specified entity."]
)
class ServiceGenerator : AbstractFieldBasedGenerator() {
    override val templateNames = listOf("service")
}
