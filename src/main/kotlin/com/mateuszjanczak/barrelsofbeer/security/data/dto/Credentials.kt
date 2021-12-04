package com.mateuszjanczak.barrelsofbeer.security.data.dto

import com.mateuszjanczak.barrelsofbeer.security.data.dto.validation.ValidPassword
import javax.validation.constraints.NotBlank

data class Credentials(
    @field:NotBlank(message = "Username cannot be empty.") val username: String,
    @field:ValidPassword val password: String
)
