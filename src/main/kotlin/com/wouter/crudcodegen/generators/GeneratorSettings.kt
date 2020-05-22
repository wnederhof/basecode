package com.wouter.crudcodegen.generators

import com.wouter.crudcodegen.engine.Variable

data class GeneratorSettings (
    val updatedProperties: ProjectProperties,
    val variables: List<Variable>
)