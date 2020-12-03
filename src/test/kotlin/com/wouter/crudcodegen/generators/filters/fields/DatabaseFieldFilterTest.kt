package com.wouter.crudcodegen.generators.filters.fields

import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.EntityType.STRING
import com.wouter.crudcodegen.generators.filters.EntityField.PrimitiveEntityField
import com.wouter.crudcodegen.generators.filters.EntityField.RelationalEntityField
import com.wouter.crudcodegen.generators.filters.FieldTemplateFilter.FieldTemplateSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class DatabaseFieldFilterTest {
    @InjectMocks
    private lateinit var databaseFieldFilter: DatabaseFieldFilter

    @Test
    fun `enrichProperties yields the fieldDatabaseDefinitionType INT NOT NULL for any relation`() {
        val fields = listOf(RelationalEntityField("userId", "User"))
        val settings = FieldTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val singleActualVariable = databaseFieldFilter.enrichProperties(0, settings).single()

        assertThat(singleActualVariable.name).isEqualTo("fieldDatabaseDefinitionType")
        assertThat(singleActualVariable.value).isEqualTo("INT NOT NULL")
    }

    @Test
    fun `enrichProperties may yield a different fieldDatabaseDefinitionType for a primitive than for a relation`() {
        val fields = listOf(PrimitiveEntityField("name", STRING))
        val settings = FieldTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val singleActualVariable = databaseFieldFilter.enrichProperties(0, settings).single()

        assertThat(singleActualVariable.name).isEqualTo("fieldDatabaseDefinitionType")
        assertThat(singleActualVariable.value).isEqualTo("VARCHAR(255) NOT NULL")
    }

    @ParameterizedTest
    @EnumSource(EntityType::class)
    fun `enrichProperties yields a fieldDatabaseDefinitionType for every primitive`(entityType: EntityType) {
        val fields = listOf(PrimitiveEntityField("userId", entityType))
        val settings = FieldTemplateSettings(SOME_STRING, SOME_STRING, SOME_STRING, fields)

        val singleActualVariable = databaseFieldFilter.enrichProperties(0, settings).single()

        assertThat(singleActualVariable.name).isEqualTo("fieldDatabaseDefinitionType")
    }

    private companion object {
        const val SOME_STRING = "some-irrelevant-string"
    }
}
