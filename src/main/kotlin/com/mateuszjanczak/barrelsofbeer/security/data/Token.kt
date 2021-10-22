package com.mateuszjanczak.barrelsofbeer.security.data

data class Token(
    val token: String,
    val expirationTime: Long,
    val refreshToken: String
)
