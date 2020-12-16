package com.wouter.crudcodegen.config

import com.github.jknack.handlebars.EscapingStrategy
import com.github.jknack.handlebars.Handlebars
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HandlebarsConfig {

    @Bean
    fun handlebars(): Handlebars {
        // Escaping, by default, is only appropriate for html.
        // As CCG handles different use cases and we are the owner
        // of the substitution functions, disabling Handlebars
        // makes Handlebars a lot easier to work with.
        return Handlebars()
            .with(EscapingStrategy.NOOP)
    }

}
