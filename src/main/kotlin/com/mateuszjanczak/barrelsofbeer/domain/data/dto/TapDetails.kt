package com.mateuszjanczak.barrelsofbeer.domain.data.dto

import com.mateuszjanczak.barrelsofbeer.common.ContentType
import org.hibernate.validator.constraints.Range

data class TapDetails(
    val contentType: ContentType,
    @field:Range(min = 1L, message = "Capacity must be a positive number.")
    val capacity: Long
) {
    val contentTypeAsString: String
        get() = contentType.name.replace("_", " ")
}