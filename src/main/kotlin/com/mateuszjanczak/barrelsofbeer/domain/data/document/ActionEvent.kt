package com.mateuszjanczak.barrelsofbeer.domain.data.document

import com.mateuszjanczak.barrelsofbeer.common.LogType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document
data class ActionEvent (
    @Id
    val id: String,
    val tapId: Int,
    val barrelContent: String,
    val currentLevel: Long,
    val totalUsage: Long,
    val singleUsage: Long,
    val date: Date,
    val logType: LogType
)