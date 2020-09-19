package com.wouter.crudcodegen.generators.impl

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.Generator
import com.wouter.crudcodegen.generators.GeneratorSettings
import com.wouter.crudcodegen.generators.ProjectProperties
import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.File

@Component
@Order(5)
class FrontendGenerator(nameHelper: NameHelper) : Generator {

    override fun getSyntax() =
            "frontend <artifactId>"

    override fun getSyntaxDescription() =
            "Generates new project called <artifactId>. " +
                    "For instance: \"new example\"."

    override fun acceptsGeneratorName(name: String) = name == "new"

    override fun templateName() = "new"

    override fun initializeGenerator(targetPath: File, properties: ProjectProperties, args: List<String>): GeneratorSettings {
        val (artifactId) = args

        return GeneratorSettings(
                updatedProperties = properties.copy(artifactId = artifactId),
                variables = listOf(
                        Variable("artifactId", artifactId)
                ))
    }

}
