package com.wouter.crudcodegen.generator.project

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.wouter.crudcodegen.generator.project.CommandLineInterpreter
import com.wouter.crudcodegen.generator.project.ProjectFilesGenerator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File

@ExtendWith(MockitoExtension::class)
internal class CommandLineInterpreterTest {
    @Mock
    private lateinit var projectFilesGenerator: ProjectFilesGenerator
    @InjectMocks
    private lateinit var commandLineInterpreter: CommandLineInterpreter

    @Test
    fun `generateNew accepts new and generates a project`() {
        val someCurrentPath = mock<File>()

        commandLineInterpreter.interpret(someCurrentPath, listOf("new", "com.hello", "world"))

        verify(projectFilesGenerator).generateNew(someCurrentPath, "com.hello", "world")
    }
}