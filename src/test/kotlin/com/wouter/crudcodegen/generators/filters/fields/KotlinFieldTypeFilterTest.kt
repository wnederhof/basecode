package com.wouter.crudcodegen.generators.filters.fields

import com.nhaarman.mockitokotlin2.whenever
import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter.FieldTemplateSettings
import com.wouter.crudcodegen.generators.helpers.NameHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class KotlinFieldTypeFilterTest {
    @Mock
    private lateinit var nameHelper: NameHelper

    @InjectMocks
    private lateinit var kotlinFieldTypeFilter: KotlinFieldTypeFilter

    @Test
    fun `enrichProperties for primitives has a fixed value`() {
        val fields = listOf(EntityField.RelationalEntityField("userId", "User"))

        val actual = kotlinFieldTypeFilter.enrichProperties(
            0,
            FieldTemplateSettings(fields)
        )

        assertThat(actual).hasSize(4)

        assertThat(actual.single { it.name == "fieldKotlinAnnotations" }.value).isEqualTo(null)
        assertThat(actual.single { it.name == "fieldKotlinType" }.value).isEqualTo("Int")
        assertThat(actual.single { it.name == "fieldKotlinTypeNotNullable" }.value).isEqualTo("Int")
        assertThat(actual.single { it.name == "fieldKotlinTestDummyValue" }.value).isEqualTo("10")
    }

    @Test
    fun `enrichProperties for relations is based on the type`() {
        whenever(nameHelper.toUpperCamelCase("about")).thenCallRealMethod()

        val fields = listOf(EntityField.PrimitiveEntityField("about", EntityType.NULL_TEXT))
        val fieldTemplateSettings = FieldTemplateSettings(fields)

        val actual = kotlinFieldTypeFilter.enrichProperties(0, fieldTemplateSettings)

        assertThat(actual).hasSize(4)

        assertThat(actual.single { it.name == "fieldKotlinAnnotations" }.value).isEqualTo("@Lob")
        assertThat(actual.single { it.name == "fieldKotlinType" }.value).isEqualTo("String?")
        assertThat(actual.single { it.name == "fieldKotlinTypeNotNullable" }.value).isEqualTo("String")
        assertThat(actual.single { it.name == "fieldKotlinTestDummyValue" }.value).isEqualTo(""""Some About"""")
    }

    private companion object {
        const val SOME_STRING = "some-irrelevant-string"
    }
}
