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
class FieldTypePropertiesFilterTest {

    @InjectMocks
    private lateinit var fieldTypePropertiesFilter: FieldTypePropertiesFilter

    @Test
    fun `enrichProperties yields false isFieldNullable if the field is not nullable`() {
        val fields = listOf(EntityField.PrimitiveEntityField("dateOfBirth", EntityType.DATE))

        val actual = fieldTypePropertiesFilter.enrichProperties(0,
                FieldTemplateSettings(fields))

        assertThat(actual.single { it.name == "isFieldNullable" }.value).isEqualTo(false)
    }

    @Test
    fun `enrichProperties yields true isFieldNullable if the field is nullable`() {
        val fields = listOf(EntityField.PrimitiveEntityField("dateOfBirth", EntityType.NULL_DATE))

        val actual = fieldTypePropertiesFilter.enrichProperties(0,
                FieldTemplateSettings(fields))

        assertThat(actual.single { it.name == "isFieldNullable" }.value).isEqualTo(true)
    }

    @Test
    fun `enrichProperties yields false isFieldOfTypeText if the field is not of type text`() {
        val fields = listOf(EntityField.PrimitiveEntityField("dateOfBirth", EntityType.INT))

        val actual = fieldTypePropertiesFilter.enrichProperties(0,
                FieldTemplateSettings(fields))

        assertThat(actual.single { it.name == "isFieldOfTypeText" }.value).isEqualTo(false)
    }

    @Test
    fun `enrichProperties yields true isFieldOfTypeText if the field is of type text`() {
        val fields = listOf(EntityField.PrimitiveEntityField("dateOfBirth", EntityType.TEXT))

        val actual = fieldTypePropertiesFilter.enrichProperties(0,
                FieldTemplateSettings(fields))

        assertThat(actual.single { it.name == "isFieldOfTypeText" }.value).isEqualTo(true)
    }

    @Test
    fun `enrichProperties yields true isFieldOfTypeText if the field is of type nullable text`() {
        val fields = listOf(EntityField.PrimitiveEntityField("dateOfBirth", EntityType.NULL_TEXT))

        val actual = fieldTypePropertiesFilter.enrichProperties(0,
                FieldTemplateSettings(fields))

        assertThat(actual.single { it.name == "isFieldOfTypeText" }.value).isEqualTo(true)
    }

    @Test
    fun `enrichProperties yields false isFieldRelational if the field is not relational`() {
        val fields = listOf(EntityField.PrimitiveEntityField("dateOfBirth", EntityType.DATE))

        val actual = fieldTypePropertiesFilter.enrichProperties(0,
                FieldTemplateSettings(fields))

        assertThat(actual.single { it.name == "isFieldRelational" }.value).isEqualTo(false)
    }

    @Test
    fun `enrichProperties yields true isFieldRelational if the field is relational`() {
        val fields = listOf(EntityField.RelationalEntityField("userId", "User"))

        val actual = fieldTypePropertiesFilter.enrichProperties(0,
                FieldTemplateSettings(fields))

        assertThat(actual.single { it.name == "isFieldRelational" }.value).isEqualTo(true)
    }

    private companion object {
        const val SOME_STRING = "some-irrelevant-string"
    }
}
