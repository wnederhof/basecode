package com.wouter.crudcodegen.generators

data class ProjectProperties (
    val groupId: String? = null,
    val artifactId: String? = null,
    val theme: Theme = Theme.plain
) {
    enum class Theme {
        plain,
        bootstrap
    }
}
