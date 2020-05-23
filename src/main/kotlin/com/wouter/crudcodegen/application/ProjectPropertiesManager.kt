package com.wouter.crudcodegen.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.wouter.crudcodegen.engine.FileManager
import com.wouter.crudcodegen.generators.ProjectProperties
import org.springframework.stereotype.Service
import java.io.File

@Service
class ProjectPropertiesManager(private val fileManager: FileManager) {
    private val yamlObjectMapper = ObjectMapper(YAMLFactory())

    fun readProperties(contextPath: File): ProjectProperties {
        val contents = fileManager.readTargetFile(contextPath, PROPERTIES_FILE_NAME)
                ?: return ProjectProperties()

        return yamlObjectMapper.readValue(contents, ProjectProperties::class.java)
    }

    fun writeProperties(contextPath: File, properties: ProjectProperties) {
        fileManager.writeTargetFileContent(contextPath, PROPERTIES_FILE_NAME,
                yamlObjectMapper.writeValueAsString(properties), true)
    }

    companion object {
        const val PROPERTIES_FILE_NAME = "crudcodegen.yml"
    }
}