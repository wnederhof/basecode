package com.wouter.crudcodegen.generators.helpers

import com.wouter.crudcodegen.generators.filters.EntityField
import org.springframework.stereotype.Service

@Service
class FieldArgsHelper {

    fun mapArgsToEntityFields(args: List<String>): List<EntityField> {
        return args
                .drop(1)
                .map { it.split(":") }
                .map { EntityField.fromUserInput(it[0], it[1]) }
    }

}
