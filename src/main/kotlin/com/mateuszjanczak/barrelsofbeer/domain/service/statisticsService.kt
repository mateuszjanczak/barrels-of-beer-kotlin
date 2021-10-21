package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ
import com.mateuszjanczak.barrelsofbeer.domain.data.document.ActionEvent
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.Ranking
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.Statistics
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.StatisticsData
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.ActionEventRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.temporal.ChronoUnit.MINUTES

interface StatisticsService {
    fun getRanking(): List<Ranking>
    fun getStatistics(fromDate: LocalDateTime, toDate: LocalDateTime, interval: Long): List<Statistics>
}

@Service
class DefaultStatisticsService(
    private val actionEventRepository: ActionEventRepository
) : StatisticsService {

    override fun getRanking(): List<Ranking> {
        val actionEventReadList = getActionEventReadList()
        val groupedByBarrelContent = getGroupedByContent(actionEventReadList)

        return groupedByBarrelContent.map { (barrelContent, list) ->
            Ranking(barrelContent, list.sumOf { it.singleUsage })
        }.sortedByDescending { it.count }
    }

    override fun getStatistics(fromDate: LocalDateTime, toDate: LocalDateTime, interval: Long): List<Statistics> {
        val actionEventReadList = getActionEventReadList()
        val groupedByBarrelContent = getGroupedByContent(actionEventReadList)
        val groupedByBarrelContentAndDate = getGroupedByBarrelContentAndDate(groupedByBarrelContent, fromDate, toDate, interval)

        return getStatisticsList(groupedByBarrelContentAndDate)
    }

    private fun getStatisticsList(groupedByBarrelContentAndDate: Map<String, Map<String, List<ActionEvent>>>): List<Statistics> {
        val statisticsList = mutableListOf<Statistics>()

        groupedByBarrelContentAndDate.forEach { (barrelContent, map) ->
            val statisticsItems = mutableListOf<StatisticsData>()

            map.forEach { (dates, list) ->
                val count = list.sumOf { it.singleUsage }
                val statisticsData = StatisticsData(dates, count)
                statisticsItems.add(statisticsData)
            }

            statisticsItems.sortBy { it.date }

            statisticsList.add(Statistics(barrelContent, statisticsItems))
        }

        return statisticsList
    }

    private fun getGroupedByBarrelContentAndDate(
        groupedByBarrelContent: Map<String, List<ActionEvent>>,
        fromDate: LocalDateTime,
        toDate: LocalDateTime,
        interval: Long
    ): Map<String, Map<String, List<ActionEvent>>> {
        val groupedByBarrelContentAndDate = mutableMapOf<String, Map<String, List<ActionEvent>>>()

        groupedByBarrelContent.forEach { (barrelContent, list) ->
            val map = mutableMapOf<String, List<ActionEvent>>()

            var next = fromDate.toInstant(UTC)
            val end = toDate.toInstant(UTC)

            while (next.plus(interval, MINUTES).also { next = it }.isBefore(end.plus(interval, MINUTES))) {
                val from = next.minus(interval, MINUTES)
                val to = next
                val actionEvents = list.filter { actionEvent ->
                    actionEvent.date.toInstant(UTC).isAfter(from) && actionEvent.date.toInstant(UTC).isBefore(to)
                }
                val key = "$from - $to"
                map[key] = actionEvents
            }

            groupedByBarrelContentAndDate[barrelContent] = map
        }
        return groupedByBarrelContentAndDate
    }

    private fun getActionEventReadList() = actionEventRepository.findAll().filter { actionEvent -> actionEvent.logType == TAP_READ }

    private fun getGroupedByContent(actionEventReadList: List<ActionEvent>) =
        actionEventReadList.groupBy { it.barrelContent }

}
