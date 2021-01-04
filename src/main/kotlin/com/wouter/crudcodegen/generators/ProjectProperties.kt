package com.wouter.crudcodegen.generators

data class ProjectProperties(
    val groupId: String? = null,
    val artifactId: String? = null,
    val components: Components = Components.plain
) {
    enum class Components {
        plain,
        bootstrap
    }
}
