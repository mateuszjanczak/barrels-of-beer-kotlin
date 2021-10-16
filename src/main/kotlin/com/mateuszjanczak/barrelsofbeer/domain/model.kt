package com.mateuszjanczak.barrelsofbeer.domain

data class SensorData(
    val value: String
)

data class SensorProperties(
    val currentLevel: Long,
    val temperature: Float
)