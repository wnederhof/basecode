package com.wouter.crudcodegen.generator.engine

import com.wouter.crudcodegen.generator.io.FileManager
import org.springframework.stereotype.Service
import java.io.File

@Service
class TemplateEngine(
        private val fileManager: FileManager,
        private val templateSolidifier: TemplateSolidifier,
        private val fileNameTemplateSolidifier: FileNameTemplateSolidifier
) {
    fun generate(targetRoot: File, templateName: String, memoryFields: List<MemoryField>) {
        val files = fileManager.listTemplateFilesRecursively(templateName)
        files.forEach { templateFilePath ->
            val generatedFilename = fileNameTemplateSolidifier.generate(templateFilePath, memoryFields)
            if (isTemplateFileName(generatedFilename)) {
                val template = fileManager.readTemplate(templateName, templateFilePath)
                val generatedContent = templateSolidifier.generate(template, memoryFields)
                val targetFileName = removeTemplateSuffix(generatedFilename)

                fileManager.writeTargetFileContent(targetRoot, targetFileName, generatedContent)
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