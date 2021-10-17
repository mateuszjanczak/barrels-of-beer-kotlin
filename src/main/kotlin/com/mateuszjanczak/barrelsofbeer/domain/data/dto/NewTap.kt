package com.mateuszjanczak.barrelsofbeer.domain.data.dto

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.NotNull

data class NewTap(
    @Range(min = 1) @NotNull val tapId: Int
)