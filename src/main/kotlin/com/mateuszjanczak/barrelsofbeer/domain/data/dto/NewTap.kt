package com.mateuszjanczak.barrelsofbeer.domain.data.dto

import org.hibernate.validator.constraints.Range

data class NewTap(
    @field:Range(min = 1, message = "Tap id must be a positive number.")
    val tapId: Int
)