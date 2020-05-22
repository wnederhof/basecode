package com.wouter.crudcodegen.generator.project

import com.wouter.crudcodegen.generator.engine.MemoryField
import com.wouter.crudcodegen.generator.engine.TemplateEngine
import org.springframework.stereotype.Service
import java.io.File

@Service
class ProjectFilesGenerator(private val templateEngine: TemplateEngine) {
    fun generateNew(target: File, groupId: String, artifactId: String) {
        templateEngine.generate(target, "new", setupVariables(
                groupId = groupId,
                artifactId = artifactId
        ))
    }

    fun generateScaffold(target: File, name: String, fieldDeclaration: List<FieldDeclaration>) {
        templateEngine.generate(target, "scaffold", setupVariables(name))
    }

    private fun setupVariables(
            groupId: String? = null,
            artifactId: String? = null,
            name: String? = null
    ): List<MemoryField> {
        return listOfNotNull(
                groupId?.let { MemoryField("groupId", it) },
                artifactId?.let { MemoryField("artifactId", it) },
                name?.let { MemoryField("name", it) },
                name?.let { MemoryField("Name", it.take(1).toUpperCase() + it.drop(1)) },
                name?.let { MemoryField("NAME", it.toUpperCase()) }
        )
    }
}