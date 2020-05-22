package com.wouter.crudcodegen.application

import com.nhaarman.mockitokotlin2.*
import com.wouter.crudcodegen.generators.Generator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File

@ExtendWith(MockitoExtension::class)
internal class CommandLineInterfaceTest {
    @Mock
    private lateinit var generators: List<Generator>

    @Mock
    private lateinit var generatorExecutor: GeneratorExecutor

    @InjectMocks
    private lateinit var commandLineInterface: CommandLineInterface

    @Test
    fun `When no args are provided, exit`() {
        val someGenerator = mock<Generator>()
        whenever(generators.iterator()).thenReturn(listOf(someGenerator).iterator())

        commandLineInterface.interpret(mock(), listOf())

        verify(generatorExecutor, times(0))
                .execute(any(), any(), any())

        verify(someGenerator, times(1))
                .getSyntax()
    }

    @Test
    fun `When args are provided, execute generator, print syntax if returns false`() {
        val someGenerator = mock<Generator>()
        whenever(generators.iterator()).thenReturn(listOf(someGenerator).iterator())

        val someCurrentPath = mock<File>()

        whenever(generatorExecutor.execute(someCurrentPath, "new", listOf("com.hello", "world")))
                .thenReturn(false)

        commandLineInterface.interpret(someCurrentPath, listOf("new", "com.hello", "world"))

        verify(someGenerator, times(1))
                .getSyntax()
    }

    @Test
    fun `When args are provided, execute generator, dont print syntax if returns true`() {
        val someCurrentPath = mock<File>()

        whenever(generatorExecutor.execute(someCurrentPath, "new", listOf("com.hello", "world")))
                .thenReturn(true)

        commandLineInterface.interpret(someCurrentPath, listOf("new", "com.hello", "world"))

        verify(generators, times(0)).iterator()
    }
}