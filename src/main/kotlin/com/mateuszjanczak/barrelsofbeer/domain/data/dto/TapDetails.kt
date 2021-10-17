package com.mateuszjanczak.barrelsofbeer.domain.data.dto

import com.mateuszjanczak.barrelsofbeer.common.ContentType
import org.hibernate.validator.constraints.Range
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TapDetails(
    @NotBlank @NotNull private val _contentType: ContentType,
    @Range(min = 1L) @NotNull val capacity: Long
) {
    val contentType: String
        get() = _contentType.name.replace("_", " ")
}