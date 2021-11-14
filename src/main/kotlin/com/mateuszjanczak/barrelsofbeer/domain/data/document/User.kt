package com.mateuszjanczak.barrelsofbeer.domain.data.document

import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    @Id val id: String = ObjectId.get().toString(),
    @Indexed(unique = true) val username: String,
    @JsonIgnore val password: String,
    val enabled: Boolean
)