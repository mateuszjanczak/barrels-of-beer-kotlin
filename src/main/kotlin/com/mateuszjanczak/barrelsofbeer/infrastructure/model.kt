package com.mateuszjanczak.barrelsofbeer.infrastructure

data class SensorResponse(
    val data: SRData
)

data class SRData(
    val value: String
)