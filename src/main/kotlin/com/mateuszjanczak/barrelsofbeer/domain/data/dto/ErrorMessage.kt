package com.mateuszjanczak.barrelsofbeer.domain.data.dto

data class ErrorMessage(
    val message: String,
    val error: String
) {
    fun toJson(): String = """{ "error": "$error", "message": "${message.replace("\"", "'")}" }"""
}