package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.ContentRanking
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.ActionEventRepository
import org.springframework.stereotype.Service

interface StatisticsService {
    fun getRanking(): List<ContentRanking>
}

@Service
class DefaultStatisticsService(
    private val actionEventRepository: ActionEventRepository
) : StatisticsService {

    override fun getRanking(): List<ContentRanking> {
        val actionEventReadList = actionEventRepository.findAll().filter { actionEvent -> actionEvent.logType == TAP_READ }

        val groupedByBarrelContent = actionEventReadList.groupBy { it.barrelContent }

        return groupedByBarrelContent.map { (barrelContent, list) ->
            ContentRanking(barrelContent, list.sumOf { it.singleUsage })
        }
    }

}
