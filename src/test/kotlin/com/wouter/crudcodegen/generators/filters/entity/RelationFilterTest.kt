package com.wouter.crudcodegen.generators.filters.entity

import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.filters.EntityField
import com.wouter.crudcodegen.generators.filters.EntityTemplateFilter.EntityTemplateSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class RelationFilterTest {

    @InjectMocks
    private lateinit var relationFilter: RelationFilter

    @Test
    fun `enrichProperties yields false hasRelations if there are no fields`() {
        val fields = listOf<EntityField>()
        val settings = EntityTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val actual = relationFilter.enrichProperties(settings)

        assertThat(actual.single().name).isEqualTo("hasRelations")
        assertThat(actual.single().value).isEqualTo(false)
    }

    @Test
    fun `enrichProperties yields false hasRelations if there are only non-relational fields`() {
        val someField = EntityField.PrimitiveEntityField("age", EntityType.INT)
        val fields = listOf(someField)
        val settings = EntityTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val actual = relationFilter.enrichProperties(settings)

        assertThat(actual.single().name).isEqualTo("hasRelations")
        assertThat(actual.single().value).isEqualTo(false)
    }

    @Test
    fun `enrichProperties yields true hasRelations if there are only relational fields`() {
        val someField = EntityField.RelationalEntityField("userId", "User")
        val fields = listOf(someField)
        val settings = EntityTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val actual = relationFilter.enrichProperties(settings)

        assertThat(actual.single().name).isEqualTo("hasRelations")
        assertThat(actual.single().value).isEqualTo(true)
    }

    @Test
    fun `enrichProperties yields true hasRelations if there is there are any relational fields`() {
        val somePrimitiveField = EntityField.PrimitiveEntityField("age", EntityType.INT)
        val someRelationalField = EntityField.RelationalEntityField("userId", "User")
        val fields = listOf(somePrimitiveField, someRelationalField)
        val settings = EntityTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val actual = relationFilter.enrichProperties(settings)

        assertThat(actual.single().name).isEqualTo("hasRelations")
        assertThat(actual.single().value).isEqualTo(true)
    }

    private companion object {
        const val SOME_STRING = "some-irrelevant-string"
    }
}
