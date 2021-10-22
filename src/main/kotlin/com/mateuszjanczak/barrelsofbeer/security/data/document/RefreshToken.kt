package com.mateuszjanczak.barrelsofbeer.security.data.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class RefreshToken(
    @Id val id: String = ObjectId.get().toString(),
    val refreshToken: String,
    val userId: String
)