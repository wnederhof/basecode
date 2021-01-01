package com.wouter.crudcodegen.config

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.lexer.Syntax
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PebbleConfig {

    @Bean
    fun pebble(): PebbleEngine {
        return PebbleEngine.Builder()
            .autoEscaping(false)
            .newLineTrimming(false)
            .syntax(
                Syntax.Builder()
                    .setEnableNewLineTrimming(false)
                    .build()
            )
            .build()
    }

}
