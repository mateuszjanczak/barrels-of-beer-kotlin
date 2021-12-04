package com.mateuszjanczak.barrelsofbeer.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.Locale.ENGLISH

@Configuration
class LanguageConfiguration {

    @Bean
    fun localeResolver(): SessionLocaleResolver {
        val slr = SessionLocaleResolver()
        slr.setDefaultLocale(ENGLISH)
        return slr
    }
}