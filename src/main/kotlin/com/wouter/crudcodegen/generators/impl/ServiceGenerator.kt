package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(3)
class ServiceGenerator(nameHelper: NameHelper) : AbstractRelationalGenerator(nameHelper) {
    override fun getSyntax() =
            "service <name> (<name>:<type>)+"

    override fun getSyntaxDescription() =
            "Generates a new Entity and Migration script. Available types: string, int."

    override fun acceptsGeneratorName(name: String) = name == "service"

    override fun templateName() = "service"
}