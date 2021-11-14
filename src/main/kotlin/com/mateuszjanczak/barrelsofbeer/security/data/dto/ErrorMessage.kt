package com.mateuszjanczak.barrelsofbeer.security.data.dto

data class ErrorMessage(
    val message: String,
    val error: String
) {
    fun toJson(): String = """{ "error": "$error", "message": "${message.replace("\"", "'")}" }"""
}