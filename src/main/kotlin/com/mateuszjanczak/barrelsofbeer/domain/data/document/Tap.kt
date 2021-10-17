package com.mateuszjanczak.barrelsofbeer.domain.data.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Tap(
    @Id
    val tapId: Int,
    val barrelContent: String = "No content",
    val temperature: Float = 0f,
    val currentLevel: Long = 0L,
    val capacity: Long = 0L,
    val enabled: Boolean = false
)