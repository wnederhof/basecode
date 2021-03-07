package com.wouter.crudcodegen.generators.filters.project

import com.nhaarman.mockitokotlin2.mock
import com.wouter.crudcodegen.generators.ProjectProperties
import com.wouter.crudcodegen.generators.filters.ProjectTemplateFilter.ProjectTemplateSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ProjectSettingsFilterTest {

    @InjectMocks
    private lateinit var projectSettingsFilter: ProjectSettingsFilter

    @Test
    fun `enrichProperties enriches with artifactId and groupId`() {
        val groupId = "com.mycorp"
        val artifactId = "employeemanager"
        val settings = ProjectTemplateSettings(groupId, artifactId, ProjectProperties.Components.plain, SOME_STRING, mock())
        val actual = projectSettingsFilter.enrichProperties(settings)

        assertThat(actual).hasSize(3)

        assertThat(actual.single { it.name == "groupId" }.value).isEqualTo("com.mycorp")
        assertThat(actual.single { it.name == "artifactId" }.value).isEqualTo("employeemanager")
    }

    @Test
    fun `enrichProperties sets useBootstrapComponents appropriately`() {
        val settings = ProjectTemplateSettings("", "", ProjectProperties.Components.bootstrap, SOME_STRING, mock())
        val actual = projectSettingsFilter.enrichProperties(settings)
    }

    private companion object {
        const val SOME_STRING = "some-irrelevant-string"
    }
}
