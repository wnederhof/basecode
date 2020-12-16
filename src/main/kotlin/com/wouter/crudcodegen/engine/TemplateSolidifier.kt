package com.wouter.crudcodegen.engine

import com.github.jknack.handlebars.Handlebars
import org.springframework.stereotype.Service

@Service
class TemplateSolidifier(private val handlebars: Handlebars) {
    fun solidifyTemplate(template: String, variables: List<Variable>): String {
        val variableMap = variables.map { it.name to it.value }.toMap()

        return handlebars.compileInline(template)
                .apply(variableMap)
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
