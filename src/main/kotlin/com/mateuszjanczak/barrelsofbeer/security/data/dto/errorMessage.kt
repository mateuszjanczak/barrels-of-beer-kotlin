package com.mateuszjanczak.barrelsofbeer.security.data.dto

open class ErrorMessage(
    val message: String,
    val error: String
) {
    fun toJson(): String = """{ "error": "$error", "message": "${message.replace("\"", "'")}" }"""
}

class ValidationErrorMessage(
    message: String,
    error: String,
    val validation: List<Field>
) : ErrorMessage(message, error)

data class Field(
    val field: String,
    val errors: List<String>
)