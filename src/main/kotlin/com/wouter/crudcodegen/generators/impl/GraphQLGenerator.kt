package com.wouter.crudcodegen.generators.impl

import org.springframework.stereotype.Component
import picocli.CommandLine.Command

@Component
@Command(
    name = "graphql",
    aliases = ["g"],
    description = ["Generates GraphQL definitions and resolvers according to the specified entity."],
    mixinStandardHelpOptions = true
)
class GraphQLGenerator: AbstractFieldBasedGenerator() {
    override val templateNames = listOf("graphql")
}
