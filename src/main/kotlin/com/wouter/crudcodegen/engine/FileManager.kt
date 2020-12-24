package com.wouter.crudcodegen.engine

import com.wouter.crudcodegen.Application
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException

@Service
class FileManager(private val resourceLoader: ResourceLoader) {
    var currentDir: String = System.getProperty("user.dir")

    fun listTemplateFilesRecursively(templateName: String): List<String> {
        val patternResolver = PathMatchingResourcePatternResolver()
        val classPath = patternResolver.getResource("classpath:/templates/$templateName/")

        // When templates is part of the .jar file
        val classPathFileNames = patternResolver
                .getResources("classpath:/templates/$templateName/**")
                .filterIsInstance<ClassPathResource>()
                .filter { !it.path.endsWith("/") }
                .map { it.path.substring((classPath as ClassPathResource).path.length) }

        val resourcesRootPath = Application::class.java.protectionDomain.codeSource.location.path
        val templatesDirectory = resourcesRootPath + "templates/$templateName/"

        // When using e.g. in the IDE
        val fileSystemFileNames = patternResolver
                .getResources("classpath:/templates/$templateName/**")
                .filterIsInstance<FileSystemResource>()
                .filter { !it.file.isDirectory }
                .onEach { check(it.path.startsWith(templatesDirectory)) }
                .map { it.path.substring(templatesDirectory.length) }

        return classPathFileNames + fileSystemFileNames
    }

    fun readTemplate(templateName: String, path: String): String {
        return resourceLoader.getResource("classpath:/templates/$templateName/$path")
                .inputStream
                .readAllBytes()
                .let { String(it) }
    }

    fun createTargetDirectory(root: File, path: String) {
        File(root.path + "/$path").mkdirs()
        println("[D] $path")
    }

    fun copyFile(root: File, templateName: String, templatePath: String, targetPath: String, overwrite: Boolean = true) {
        if (!overwrite && File(root.path + "/$targetPath").exists()) {
            error("File already exists: $targetPath")
        }
        val directories = targetPath.split("/").dropLast(1).joinToString("/")
        File("${root.path}/$directories").mkdirs()
        resourceLoader.getResource("classpath:/templates/$templateName/$templatePath")
                .inputStream
                .readAllBytes()
                .let { File("${root.path}/$targetPath").writeBytes(it) }
    }

    fun writeTargetFileContent(root: File, path: String, content: String, overwrite: Boolean = false) {
        if (!overwrite && File(root.path + "/$path").exists()) {
            error("File already exists: $path")
        }
        val directories = path.split("/").dropLast(1).joinToString("/")
        File("${root.path}/$directories").mkdirs()
        File("${root.path}/$path").writeText(content)
    }

    fun readTargetFile(root: File, path: String): String? {
        if (!File(root.path + "/$path").exists()) {
            return null
        }
        return File(root.path + "/$path").readText()
    }

    fun deleteFile(root: File, path: String, removeDirIfEmpty: Boolean): Boolean {
        val file = File(root.path + "/$path")
        if (file.exists()) {
            file.delete()
            if (removeDirIfEmpty && file.parentFile.listFiles()!!.isEmpty()) {
                file.parentFile.delete()
            }
            return true
        }
        return false
    }

    fun exists(root: File, path: String): Boolean {
        val file = File(root.path + "/$path")
        return file.exists()
    }
}
