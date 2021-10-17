package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.common.LogType
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_NEW
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ_TEMPERATURE
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_SET
import com.mateuszjanczak.barrelsofbeer.domain.data.document.ActionEvent
import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import com.mateuszjanczak.barrelsofbeer.domain.data.document.TemperatureEvent
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.ActionEventRepository
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TemperatureEventRepository
import org.springframework.stereotype.Service

interface EventService {
    fun saveEvent(tap: Tap, logType: LogType)
}

@Service
class DefaultEventService(
    private val actionEventRepository: ActionEventRepository,
    private val temperatureEventRepository: TemperatureEventRepository
) : EventService {

    override fun saveEvent(tap: Tap, logType: LogType) {
        when (logType) {
            TAP_NEW -> saveTapNew(tap)
            TAP_SET -> saveTapSet(tap)
            TAP_READ -> saveTapReadCurrentLevel(tap)
            TAP_READ_TEMPERATURE -> saveTapReadTemperature(tap)
        }
    }

    private fun saveTapNew(tap: Tap) {
        actionEventRepository.save(
            ActionEvent(
                tapId = tap.tapId,
                barrelContent = tap.barrelContent,
                currentLevel = tap.currentLevel,
                totalUsage = tap.capacity - tap.currentLevel,
                singleUsage = 0L,
                logType = TAP_NEW
            )
        )
    }

    private fun saveTapSet(tap: Tap) {
        actionEventRepository.save(
            ActionEvent(
                tapId = tap.tapId,
                barrelContent = tap.barrelContent,
                currentLevel = tap.currentLevel,
                totalUsage = tap.capacity - tap.currentLevel,
                singleUsage = 0L,
                logType = TAP_SET
            )
        )
    }

    private fun saveTapReadCurrentLevel(tap: Tap) {
        actionEventRepository.save(
            ActionEvent(
                tapId = tap.tapId,
                barrelContent = tap.barrelContent,
                currentLevel = tap.currentLevel,
                totalUsage = tap.capacity - tap.currentLevel,
                singleUsage = tap.capacity - tap.currentLevel - getLastTotalUsage(tap.tapId),
                logType = TAP_READ
            )
        )
    }

    private fun saveTapReadTemperature(tap: Tap) {
        temperatureEventRepository.save(
            TemperatureEvent(
                tapId = tap.tapId,
                barrelContent = tap.barrelContent,
                temperature = tap.temperature
            )
        )
    }

    private fun getLastTotalUsage(tapId: Int) = actionEventRepository.findActionEventByTapIdOrderByIdDesc(tapId).totalUsage
}