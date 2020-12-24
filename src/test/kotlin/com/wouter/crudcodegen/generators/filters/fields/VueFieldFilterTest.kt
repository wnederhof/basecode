package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class VueFieldFilterTest {
    @InjectMocks
    private lateinit var vueFieldFilter: VueFieldFilter

    @Test
    fun `enrichProperties yields the fieldHtmlInputType number for any relation`() {
        val fields = listOf(EntityField.RelationalEntityField("userId", "User"))
        val settings = FieldTemplateFilter.FieldTemplateSettings(fields)

        val singleActualVariable = vueFieldFilter.enrichProperties(0, settings).single()

        assertThat(singleActualVariable.name).isEqualTo("fieldHtmlInputType")
        assertThat(singleActualVariable.value).isEqualTo("number")
    }

    @Test
    fun `enrichProperties may yield a different fieldHtmlInputType for a primitive than for a relation`() {
        val fields = listOf(EntityField.PrimitiveEntityField("name", EntityType.STRING))
        val settings = FieldTemplateFilter.FieldTemplateSettings(fields)

        val singleActualVariable = vueFieldFilter.enrichProperties(0, settings).single()

        assertThat(singleActualVariable.name).isEqualTo("fieldHtmlInputType")
        assertThat(singleActualVariable.value).isEqualTo("text")
    }

    @ParameterizedTest
    @EnumSource(EntityType::class)
    fun `enrichProperties yields a fieldHtmlInputType for every primitive`(entityType: EntityType) {
        val fields = listOf(EntityField.PrimitiveEntityField("userId", entityType))
        val settings = FieldTemplateFilter.FieldTemplateSettings(fields)

        val singleActualVariable = vueFieldFilter.enrichProperties(0, settings).single()

        assertThat(singleActualVariable.name).isEqualTo("fieldHtmlInputType")
    }

    private companion object {
        const val SOME_STRING = "some-irrelevant-string"
    }
}
