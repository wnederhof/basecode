package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(3)
class ControllerGenerator(nameHelper: NameHelper) : AbstractRelationalGenerator(nameHelper) {
    override fun getSyntax() =
            "controller <name> (<name>:<type>)+"

    override fun getSyntaxDescription() =
            "Generates a new controller with HTML pages. Available types: string, int."

    override fun acceptsGeneratorName(name: String) = name == "controller"

    override fun templateName() = "controller"
}
