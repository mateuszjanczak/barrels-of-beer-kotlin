package com.mateuszjanczak.barrelsofbeer.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ReactWebConfiguration : WebMvcConfigurer {

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/{spring:\\w+}")
            .setViewName("forward:/")
        registry.addViewController("/**/{spring:\\w+}")
            .setViewName("forward:/")
        registry.addViewController("/{spring:\\w+}/**{spring:?!(\\.js|\\.css)$}")
            .setViewName("forward:/")
    }
}