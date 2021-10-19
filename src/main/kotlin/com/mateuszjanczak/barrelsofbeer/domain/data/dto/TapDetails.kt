package com.mateuszjanczak.barrelsofbeer.domain.data.dto

import com.mateuszjanczak.barrelsofbeer.common.ContentType
import org.hibernate.validator.constraints.Range
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TapDetails(
    @NotBlank @NotNull val contentType: ContentType,
    @Range(min = 1L) @NotNull val capacity: Long
){
    val contentTypeAsString: String
        get() = contentType.name.replace("_", " ")
}