package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(4)
class FrontendScaffoldGenerator(nameHelper: NameHelper) : AbstractRelationalGenerator(nameHelper) {

    override fun getSyntax() =
            "fe-scaffold <name> (<name>:<type>)+"

    override fun getSyntaxDescription() =
            "Generates a barebones Nuxt Frontend CRUD. Available types: string, int."

    override fun acceptsGeneratorName(name: String) = name == "frontend-scaffold"

    override fun templateName() = "frontend-scaffold"

}
