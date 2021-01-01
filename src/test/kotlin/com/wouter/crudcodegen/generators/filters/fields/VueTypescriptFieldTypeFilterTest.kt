package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter.FieldTemplateSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class VueTypescriptFieldTypeFilterTest {
    @InjectMocks
    private lateinit var vueTypescriptFieldTypeFilter: VueTypescriptFieldTypeFilter

    @Test
    fun `enrichProperties for relations has a fixed value`() {
        val fields = listOf(EntityField.PrimitiveEntityField("xyz", EntityType.STRING))

        val fieldTemplateSettings = FieldTemplateSettings(fields)

        val actual = vueTypescriptFieldTypeFilter.enrichProperties(0, fieldTemplateSettings)

        assertThat(actual).hasSize(1)
        assertThat(actual.single { it.name == "fieldVueTypescriptTestValue" }.value).isEqualTo("'Some xyz'")
    }

    @Test
    fun `enrichProperties for primitives always yields 1 for fieldVueTypescriptTestValue`() {
        val fields = listOf(EntityField.RelationalEntityField("userId", "User"))

        val fieldTemplateSettings = FieldTemplateSettings(fields)

        val actual = vueTypescriptFieldTypeFilter.enrichProperties(0, fieldTemplateSettings)

        assertThat(actual).hasSize(1)
        assertThat(actual.single { it.name == "fieldVueTypescriptTestValue" }.value).isEqualTo("1")
    }

    private companion object {
        const val SOME_STRING = "some-irrelevant-string"
    }
}
