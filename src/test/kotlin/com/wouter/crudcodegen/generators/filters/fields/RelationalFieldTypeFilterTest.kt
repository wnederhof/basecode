package com.wouter.crudcodegen.generators.filters.fields

import com.nhaarman.mockitokotlin2.whenever
import com.wouter.crudcodegen.generators.EntityType.INT
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
class RelationalFieldTypeFilterTest {
    @Mock
    private lateinit var nameHelper: NameHelper
    @InjectMocks
    private lateinit var relationalFieldTypeFilter: RelationalFieldTypeFilter

    @Test
    fun `enrichProperties returns an empty list if the field is not relational`() {
        val fields = listOf(EntityField.PrimitiveEntityField("age", INT))

        val actual = relationalFieldTypeFilter.enrichProperties(0,
                FieldTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields))

        assertThat(actual).isEmpty()
    }

    @Test
    fun `enrichProperties yields field type in pascal, lower, camel and snake case`() {
        val fields = listOf(EntityField.RelationalEntityField("salesManId", "SalesMan"))

        whenever(nameHelper.toUpperCamelCase("SalesMan")).thenCallRealMethod()
        whenever(nameHelper.toDuckName("SalesMan")).thenCallRealMethod()
        whenever(nameHelper.toLowerCamelCase("SalesMan")).thenCallRealMethod()

        val actual = relationalFieldTypeFilter.enrichProperties(0,
                FieldTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields))

        assertThat(actual).hasSize(4)

        assertThat(actual.single { it.name == "fieldTypePascalCase" }.value).isEqualTo("SalesMan")
        assertThat(actual.single { it.name == "fieldTypeLowerCase" }.value).isEqualTo("salesman")
        assertThat(actual.single { it.name == "fieldTypeScreamingSnakeCase" }.value).isEqualTo("SALES_MAN")
        assertThat(actual.single { it.name == "fieldTypeCamelCase" }.value).isEqualTo("salesMan")
    }

    private companion object {
        const val SOME_STRING = "some-irrelevant-string"
    }
}
