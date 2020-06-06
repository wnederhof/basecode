package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(3)
class GraphQLGenerator(nameHelper: NameHelper) : AbstractRelationalGenerator(nameHelper) {
    override fun getSyntax() =
            "graphql <name> (<name>:<type>)+"

    override fun getSyntaxDescription() =
            "Generates a new GraphQL Schema and resolvers. Available types: string, int."

    override fun acceptsGeneratorName(name: String) = name == "graphql"

    override fun templateName() = "graphql"
}
