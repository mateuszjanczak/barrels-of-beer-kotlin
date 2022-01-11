package com.mateuszjanczak.barrelsofbeer.security.data.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class Credentials(
    @field:NotBlank(message = "Username cannot be empty.")
    @field:Size(min = 5, max = 25, message = "Username must be between 5 and 25 characters.")
    val username: String,
    @field:NotBlank(message = "Password cannot be empty.")
    @field:Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters.")
    @field:Pattern.List(
        Pattern(regexp = ".*[0-9].*", message = "Password must contain 1 or more digit characters."),
        Pattern(regexp = ".*[a-z].*", message = "Password must contain 1 or more lowercase characters."),
        Pattern(regexp = ".*[A-Z].*", message = "Password must contain 1 or more uppercase characters."),
        Pattern(regexp = ".*[\\W].*", message = "Password must contain 1 or more special characters.")
    )
    val password: String
)
