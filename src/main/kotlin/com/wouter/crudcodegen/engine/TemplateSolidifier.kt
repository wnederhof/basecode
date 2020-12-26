package com.wouter.crudcodegen.engine

import com.mitchellbosecke.pebble.PebbleEngine
import org.springframework.stereotype.Service
import java.io.StringWriter

@Service
class TemplateSolidifier(private val pebbleEngine: PebbleEngine) {
    fun solidifyTemplate(template: String, variables: List<Variable>): String {
        val variableMap = variables.map { it.name to it.value }.toMap()

        val writer = StringWriter()

        pebbleEngine.getLiteralTemplate(template).evaluate(writer, variableMap)
        return writer.toString()
    }

    fun solidifyFilename(filename: String, variables: List<Variable>): String {
        var mutableFilename = filename

        variables.forEach {
            val newValue = "${it.value}".replace(".", "/")
            mutableFilename = mutableFilename.replace("[${it.name}]", newValue)
        }

        return mutableFilename
    }
}
