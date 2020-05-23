package com.wouter.crudcodegen.generators.helpers

import org.springframework.stereotype.Service

@Service
class NameHelper {
    fun toDuckName(name: String) = name.mapIndexed { i, c ->
        if (c.isUpperCase() && i > 0)
            "_$c".toLowerCase()
        else
            c.toLowerCase()
    }.joinToString("")

    fun toUpperCamelCase(s: String) = s.take(1).toUpperCase() + s.drop(1)

    fun toLowerCamelCase(s: String) = s.take(1).toLowerCase() + s.drop(1)

    fun pluralize(s: String) = if (s.endsWith("s")) s + "es" else s + "s"

    fun capitalize(s: String) = s.toUpperCase()
}