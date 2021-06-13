package com.wouter.crudcodegen.generators.filters.project

import com.wouter.crudcodegen.engine.Variable
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter
import org.springframework.stereotype.Component
import java.io.File

@Component
class DatabaseFilter : ProjectTemplateFilter {
    override fun enrichProperties(settings: ProjectTemplateFilter.ProjectTemplateSettings): Iterable<Variable> {
        return listOf(
            Variable("nextMigrationPrefix", findNextMigrationPrefix(settings.targetPath, settings.artifactId!!))
        )
    }

    private fun findNextMigrationPrefix(targetPath: File, artifactId: String): String {
        val i = findLastMigrationNumber(targetPath, artifactId) + 1
        return "$i".padStart(3, '0')
    }

    private fun findLastMigrationNumber(targetPath: File, artifactId: String): Int {
        val files = File("$targetPath/$artifactId-server/src/main/resources/db/migration").listFiles()
            ?: return 0

        return files
            .filter { it.name.endsWith(".sql") }
            .map { it.name }
            .map { findNumberPrefix(it) }
            .maxByOrNull { it } ?: 0
    }

    private fun findNumberPrefix(s: String): Int {
        return Integer.parseInt(s.replace(Regex("V(\\d+).+"), "\$1"))
    }
}
