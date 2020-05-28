package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(2)
class EntityGenerator(
        private val nameHelper: NameHelper
) : AbstractRelationalGenerator(nameHelper) {

    override fun getSyntax() =
            "entity <name> (<name>:<type>)+"

    override fun getSyntaxDescription() =
            "Generates a new Entity and Migration script. Available types: string, int."

    override fun acceptsGeneratorName(name: String) = name == "entity"

    override fun templateName() = "entity"

}