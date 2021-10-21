package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.Ranking
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.ActionEventRepository
import org.springframework.stereotype.Service

interface StatisticsService {
    fun getRanking(): List<Ranking>
}

@Service
class DefaultStatisticsService(
    private val actionEventRepository: ActionEventRepository
) : StatisticsService {

    override fun getRanking(): List<Ranking> {
        val actionEventReadList = actionEventRepository.findAll().filter { actionEvent -> actionEvent.logType == TAP_READ }

        val groupedByBarrelContent = actionEventReadList.groupBy { it.barrelContent }

        return groupedByBarrelContent.map { (barrelContent, list) ->
            Ranking(barrelContent, list.sumOf { it.singleUsage })
        }.sortedByDescending { it.count }
    }

}
