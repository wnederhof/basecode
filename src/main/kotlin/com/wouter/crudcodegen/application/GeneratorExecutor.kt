package com.wouter.crudcodegen.application

import com.wouter.crudcodegen.engine.TemplateEngine
import com.wouter.crudcodegen.generators.Generator
import org.springframework.stereotype.Service
import java.io.File

@Service
class GeneratorExecutor(
        private val generators: List<Generator>,
        private val templateEngine: TemplateEngine,
        private val projectPropertiesManager: ProjectPropertiesManager
) {
    fun execute(contextPath: File, name: String, args: List<String>): Boolean {
        val generator = generators.singleOrNull { it.acceptsGeneratorName(name) }
                ?: return false

        val properties = projectPropertiesManager.readProperties(contextPath)
        val generatorSettings = generator.initializeGenerator(contextPath, properties, args)

        templateEngine.generate(contextPath, generator.templateName(), generatorSettings.variables)
        projectPropertiesManager.writeProperties(contextPath, generatorSettings.updatedProperties)

        return true
    }
}