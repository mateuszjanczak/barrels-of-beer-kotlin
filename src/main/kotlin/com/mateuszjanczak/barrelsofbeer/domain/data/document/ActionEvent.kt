package com.mateuszjanczak.barrelsofbeer.domain.data.document

import com.fasterxml.jackson.annotation.JsonFormat
import com.mateuszjanczak.barrelsofbeer.common.LogType
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class ActionEvent(
    @Id
    val id: String = ObjectId.get().toString(),
    val tapId: Int,
    val barrelContent: String,
    val currentLevel: Long,
    val totalUsage: Long,
    val singleUsage: Long,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") val date: LocalDateTime = LocalDateTime.now(),
    val logType: LogType
)