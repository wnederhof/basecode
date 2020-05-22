package com.wouter.crudcodegen.generator.io

import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException

@Service
class FileManager(private val resourceLoader: ResourceLoader) {
    fun listTemplateDirectories(path: String = ""): List<String> {
        return resourceLoader.getResource("templates/$path").file.listFiles()!!
                .filter { it.isDirectory }
                .map { it.name }
    }

    fun listTemplateFiles(path: String = ""): List<String> {
        return resourceLoader.getResource("templates/$path").file.listFiles()!!
                .filter { !it.isDirectory }
                .map { it.name }
    }

    fun listTemplateFilesRecursively(templateName: String, path: String = ""): List<String> {
        val templateFiles = listTemplateFiles("$templateName/$path")
        val templateDirectories = listTemplateDirectories("$templateName/$path")
        val nestedFiles = templateDirectories
                .map { if (path == "") it else "$path/$it" }
                .flatMap { listTemplateFilesRecursively(templateName, it) }

        return templateFiles.map { if (path == "") it else "$path/$it" } + nestedFiles
    }

    fun readTemplate(templateName: String, path: String): String {
        return resourceLoader.getResource("templates/$templateName/$path").file.readText()
    }

    fun createTargetDirectory(root: File, path: String) {
        File(root.path + "/$path").mkdirs()
    }

    fun copyFile(root: File, templateName: String, templatePath: String, targetPath: String) {
        if (File(root.path + "/$targetPath").exists()) {
            throw IOException("File already exists: $targetPath")
        }
        val directories = targetPath.split("/").dropLast(1).joinToString("/")
        File("${root.path}/$directories").mkdirs()
        resourceLoader.getResource("templates/$templateName/$templatePath")
                .file
                .copyTo(File("${root.path}/$targetPath"))
    }

    fun writeTargetFileContent(root: File, path: String, content: String) {
        if (File(root.path + "/$path").exists()) {
            throw IOException("File already exists: $path")
        }
        val directories = path.split("/").dropLast(1).joinToString("/")
        File("${root.path}/$directories").mkdirs()
        File("${root.path }/$path").writeText(content)
    }
}