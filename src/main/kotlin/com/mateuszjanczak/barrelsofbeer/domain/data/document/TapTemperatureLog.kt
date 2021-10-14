package com.mateuszjanczak.barrelsofbeer.domain.data.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document
data class TapTemperatureLog(
    @Id
    val id: String,
    val tapId: Int,
    val barrelContent: String,
    val temperature: Float,
    val date: Date
)