package com.wouter.crudcodegen.engine

import org.springframework.stereotype.Service
import java.io.File

@Service
class TemplateEngine(
        private val fileManager: FileManager,
        private val templateSolidifier: TemplateSolidifier
) {
    fun generate(targetRoot: File, templateName: String, variables: List<Variable>) {
        val files = fileManager.listTemplateFilesRecursively(templateName)
        files.forEach { templateFilePath ->
            val generatedFilename = templateSolidifier.solidifyFilename(templateFilePath, variables)
            if (isTemplateFileName(generatedFilename)) {
                val template = fileManager.readTemplate(templateName, templateFilePath)
                val targetFileName = removeTemplateSuffix(generatedFilename)
                println("[F] $targetFileName")
                val generatedContent = templateSolidifier.solidifyTemplate(template, variables)
                if (generatedContent.trim() != "") {
                    fileManager.writeTargetFileContent(targetRoot, targetFileName, generatedContent)
                }
            } else {
                fileManager.copyFile(targetRoot, templateName, templateFilePath, generatedFilename)
            }
        }
    }

    private fun isTemplateFileName(targetFileName: String) =
            targetFileName.endsWith(TEMPLATE_SUFFIX)

    private fun removeTemplateSuffix(targetFileName: String) =
            targetFileName.dropLast(TEMPLATE_SUFFIX.length)

    private companion object {
        const val TEMPLATE_SUFFIX = ".hbs"
    }
}
