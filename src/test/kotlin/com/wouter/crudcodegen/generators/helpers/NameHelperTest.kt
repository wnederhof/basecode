package com.wouter.crudcodegen.generators.helpers

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class NameHelperTest {
    @InjectMocks
    private lateinit var nameHelper: NameHelper

    @Test
    fun toDuckName() {
        assertThat(
            nameHelper.toDuckCase("wouterNederhof"),
            equalTo("wouter_nederhof")
        )
        assertThat(
            nameHelper.toDuckCase("WouterNederhof"),
            equalTo("wouter_nederhof")
        )
    }

    @Test
    fun toDashName() {
        assertThat(
            nameHelper.toDashCase("wouterNederhof"),
            equalTo("wouter-nederhof")
        )
        assertThat(
            nameHelper.toDashCase("WouterNederhof"),
            equalTo("wouter-nederhof")
        )
    }

    @Test
    fun toCapitalCamelCase() {
        assertThat(
            nameHelper.toUpperCamelCase("wouterNederhof"),
            equalTo("WouterNederhof")
        )
        assertThat(
            nameHelper.toUpperCamelCase("WouterNederhof"),
            equalTo("WouterNederhof")
        )
    }

    @Test
    fun toLowerCamelCase() {
        assertThat(
            nameHelper.toLowerCamelCase("wouterNederhof"),
            equalTo("wouterNederhof")
        )
        assertThat(
            nameHelper.toLowerCamelCase("WouterNederhof"),
            equalTo("wouterNederhof")
        )
    }

    @Test
    fun pluralize() {
        assertThat(
            nameHelper.pluralize("post"),
            equalTo("posts")
        )

        assertThat(
            nameHelper.pluralize("bass"),
            equalTo("basses")
        )
    }

    @Test
    fun capitalize() {
        assertThat(
            nameHelper.capitalize("post"),
            equalTo("POST")
        )
    }
}
