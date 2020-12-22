package com.wouter.crudcodegen.engine

import org.springframework.stereotype.Service
import java.io.File

@Service
class TemplateEngine(
        private val fileManager: FileManager,
        private val templateSolidifier: TemplateSolidifier
) {
    fun findAlreadyExistingTargetFiles(targetRoot: File, templateName: String, variables: List<Variable>): List<String> {
        val files = fileManager.listTemplateFilesRecursively(templateName)
        return files.mapNotNull { templateFilePath ->
            val generatedFilename = templateSolidifier.solidifyFilename(templateFilePath, variables)
            if (isTemplateFileName(generatedFilename)) {
                val targetFileName = removeTemplateSuffix(generatedFilename)
                if (fileManager.exists(targetRoot, targetFileName)
                        && !File(targetRoot.path + "/$targetFileName").isDirectory) {
                    targetFileName
                } else {
                    null
                }
            } else {
                if (fileManager.exists(targetRoot, generatedFilename)
                        && !File(targetRoot.path + "/$generatedFilename").isDirectory) {
                    generatedFilename
                } else {
                    null
                }
            }
        }
    }

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
                    fileManager.writeTargetFileContent(targetRoot, targetFileName, generatedContent, true)
                }
            } else {
                println("[C] $generatedFilename")
                fileManager.copyFile(targetRoot, templateName, templateFilePath, generatedFilename, true)
            }
        }
    }

    fun delete(targetRoot: File, templateName: String, variables: List<Variable>) {
        val files = fileManager.listTemplateFilesRecursively(templateName)
        files.forEach { templateFilePath ->
            val generatedFilename = templateSolidifier.solidifyFilename(templateFilePath, variables)
            if (isTemplateFileName(generatedFilename)) {
                val targetFileName = removeTemplateSuffix(generatedFilename)
                if (!File(targetRoot.path + "/$targetFileName").isDirectory
                    && fileManager.deleteFile(targetRoot, targetFileName, true)) {
                    println("[D] $targetFileName")
                }
            } else {
                if (!File(targetRoot.path + "/$generatedFilename").isDirectory
                    && fileManager.deleteFile(targetRoot, generatedFilename, true)) {
                    println("[D] $generatedFilename")
                }
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
