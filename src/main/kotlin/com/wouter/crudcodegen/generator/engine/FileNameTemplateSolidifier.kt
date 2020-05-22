package com.wouter.crudcodegen.generator.engine

import org.springframework.stereotype.Service

@Service
class FileNameTemplateSolidifier {
    fun generate(filename: String, memoryFields: List<MemoryField>): String {
        var mutableFilename = filename

        memoryFields.forEach {
            val newValue = "${it.value}".replace(".", "/")
            mutableFilename = mutableFilename.replace("[${it.name}]", newValue)
        }

        return mutableFilename
    }
}