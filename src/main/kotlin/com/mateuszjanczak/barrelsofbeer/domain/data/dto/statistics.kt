package com.mateuszjanczak.barrelsofbeer.domain.data.dto

data class Statistics(
    val name: String,
    val items: List<StatisticsData>
)

data class StatisticsData(
    val date: String,
    val count: Long
)
