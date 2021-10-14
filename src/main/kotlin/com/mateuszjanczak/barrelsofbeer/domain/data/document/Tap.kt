package com.mateuszjanczak.barrelsofbeer.domain.data.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Tap(
    @Id
    val tapId: Int,
    val barrelContent: String,
    val temperature: Float,
    val currentLevel: Long,
    val capacity: Long,
    val enabled: Boolean
)