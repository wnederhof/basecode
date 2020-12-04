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
class GraphQLFieldTypeFilterTest {
    @InjectMocks
    private lateinit var graphQLFieldTypeFilter: GraphQLFieldTypeFilter

    @Test
    fun `enrichProperties yields Int, Int! for any relation`() {
        val fields = listOf(EntityField.RelationalEntityField("userId", "User"))
        val settings = FieldTemplateFilter.FieldTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val actual = graphQLFieldTypeFilter.enrichProperties(0, settings)

        assertThat(actual).hasSize(2)

        assertThat(actual.single { it.name == "nullableGraphQLFieldType" }.value).isEqualTo("Int")
        assertThat(actual.single { it.name == "graphQLFieldType" }.value).isEqualTo("Int!")
    }

    @ParameterizedTest
    @EnumSource(EntityType::class)
    fun `enrichProperties always produces nullableGraphQLFieldType and graphQLFieldType fields for primitives`(entityType: EntityType) {
        val fields = listOf(EntityField.PrimitiveEntityField("name", entityType))
        val settings = FieldTemplateFilter.FieldTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val actual = graphQLFieldTypeFilter.enrichProperties(0, settings)

        assertThat(actual).hasSize(2)

        assertThat(actual).anyMatch { it.name == "nullableGraphQLFieldType" }
        assertThat(actual).anyMatch { it.name == "graphQLFieldType" }
    }

    @ParameterizedTest
    @EnumSource(EntityType::class)
    fun `enrichProperties never ends with exclamation mark for nullableGraphQLFieldType`(entityType: EntityType) {
        val fields = listOf(EntityField.PrimitiveEntityField("name", entityType))
        val settings = FieldTemplateFilter.FieldTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val actual = graphQLFieldTypeFilter.enrichProperties(0, settings)

        assertThat(actual).hasSize(2)

        assertThat(actual).noneMatch { it.name == "nullableGraphQLFieldType" && ("" + it.value).endsWith("!") }
    }

    @ParameterizedTest
    @EnumSource(EntityType::class)
    fun `enrichProperties ends with exclamation mark for graphQLFieldType for non-nullable types`(entityType: EntityType) {
        val fields = listOf(EntityField.PrimitiveEntityField("name", entityType))
        val settings = FieldTemplateFilter.FieldTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val actual = graphQLFieldTypeFilter.enrichProperties(0, settings)

        assertThat(actual).hasSize(2)

        if (entityType.isNullable) {
            assertThat(actual).noneMatch { it.name == "graphQLFieldType" && ("" + it.value).endsWith("!") }
        } else {
            assertThat(actual).noneMatch { it.name == "graphQLFieldType" && !("" + it.value).endsWith("!") }
        }
    }

    private companion object {
        const val SOME_STRING = "some-irrelevant-string"
    }
}
