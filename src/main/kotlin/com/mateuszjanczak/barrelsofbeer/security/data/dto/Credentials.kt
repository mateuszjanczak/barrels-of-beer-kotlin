package com.mateuszjanczak.barrelsofbeer.security.data.dto

import com.mateuszjanczak.barrelsofbeer.security.data.dto.validation.ValidPassword
import javax.validation.constraints.NotBlank

data class Credentials(
    @NotBlank val username: String,
    @NotBlank @ValidPassword val password: String
)
