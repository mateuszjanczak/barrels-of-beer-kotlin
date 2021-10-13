package com.mateuszjanczak.barrelsofbeer.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SensorConfiguration(
    @Value("\${sensor.baseUrl}") val baseUrl: String,
    @Value("\${sensor.timeout}") val timeout: Double
)