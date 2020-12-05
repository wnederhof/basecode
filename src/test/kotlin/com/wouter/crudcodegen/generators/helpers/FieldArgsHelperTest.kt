package com.wouter.crudcodegen.generators.helpers

import com.wouter.crudcodegen.generators.EntityType
import com.wouter.crudcodegen.generators.filters.EntityField.PrimitiveEntityField
import com.wouter.crudcodegen.generators.filters.EntityField.RelationalEntityField
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FieldArgsHelperTest {
    @InjectMocks
    private lateinit var fieldArgsHelper: FieldArgsHelper

    @Test
    fun `mapArgsToEntityFields creates entity fields from CLI arguments`() {
        val actual = fieldArgsHelper.mapArgsToEntityFields(listOf("name:string", "userId:User"))

        val expected = listOf(
                PrimitiveEntityField("name", EntityType.STRING),
                RelationalEntityField("userId", "User")
        )

        assertThat(actual).isEqualTo(expected)
    }

}
