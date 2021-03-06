package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.common.LogType
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_DISABLE
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_ENABLE
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_NEW
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ_TEMPERATURE
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_REMOVE
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_SET
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_RESET
import com.mateuszjanczak.barrelsofbeer.domain.data.document.ActionEvent
import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import com.mateuszjanczak.barrelsofbeer.domain.data.document.TemperatureEvent
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.ActionEventRepository
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TemperatureEventRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

interface EventService {
    fun saveEvent(tap: Tap, logType: LogType)
    fun saveEvent(previousTap: Tap, tap: Tap, logType: LogType)
    fun getActionEvents(): List<ActionEvent>
    fun getActionEvents(page: Int): Page<ActionEvent>
    fun getTemperatureEvents(): List<TemperatureEvent>
    fun getTemperatureEvents(page: Int): Page<TemperatureEvent>
}

@Service
class DefaultEventService(
    private val actionEventRepository: ActionEventRepository,
    private val temperatureEventRepository: TemperatureEventRepository
) : EventService {

    companion object {
        private val log = LoggerFactory.getLogger(DefaultEventService::class.java)
    }

    override fun saveEvent(tap: Tap, logType: LogType) {
        saveEvent(tap, logType, null)
    }

    override fun saveEvent(previousTap: Tap, tap: Tap, logType: LogType) {
        saveEvent(tap, logType, previousTap)
    }

    override fun getActionEvents(): List<ActionEvent> = actionEventRepository.findAll()

    override fun getActionEvents(page: Int): Page<ActionEvent> = actionEventRepository.findAll(PageRequest.of(page, 20))

    override fun getTemperatureEvents(): List<TemperatureEvent> = temperatureEventRepository.findAll()

    override fun getTemperatureEvents(page: Int): Page<TemperatureEvent> = temperatureEventRepository.findAll(PageRequest.of(page, 20))

    private fun saveEvent(tap: Tap, logType: LogType, previousTap: Tap?) {
        when (logType) {
            TAP_NEW -> saveTapNew(tap)
            TAP_SET -> saveTapSet(tap)
            TAP_REMOVE -> saveTapRemove(tap)
            TAP_READ -> previousTap?.let{ saveTapReadCurrentLevel(previousTap, tap)}
            TAP_READ_TEMPERATURE -> saveTapReadTemperature(tap)
            TAP_ENABLE, TAP_DISABLE -> saveToggleTap(tap, logType)
            TAP_RESET -> saveTapReset(tap)
        }.also { log.debug("New event on tap ${tap.tapId} with action ${logType.name}") }
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

    private fun saveTapRemove(tap: Tap) {
        actionEventRepository.save(
            ActionEvent(
                tapId = tap.tapId,
                barrelContent = tap.barrelContent,
                currentLevel = tap.currentLevel,
                totalUsage = tap.capacity - tap.currentLevel,
                singleUsage = 0L,
                logType = TAP_REMOVE
            )
        )
    }

    private fun saveTapReadCurrentLevel(previousTap: Tap, tap: Tap) {
        actionEventRepository.save(
            ActionEvent(
                tapId = tap.tapId,
                barrelContent = tap.barrelContent,
                currentLevel = tap.currentLevel,
                totalUsage = tap.capacity - tap.currentLevel,
                singleUsage = previousTap.currentLevel - tap.currentLevel,
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

    private fun saveToggleTap(tap: Tap, logType: LogType) {
        actionEventRepository.save(
            ActionEvent(
                tapId = tap.tapId,
                barrelContent = tap.barrelContent,
                currentLevel = tap.currentLevel,
                totalUsage = 0L,
                singleUsage = 0L,
                logType = logType
            )
        )
    }

    private fun saveTapReset(tap: Tap) {
        actionEventRepository.save(
            ActionEvent(
                tapId = tap.tapId,
                barrelContent = tap.barrelContent,
                currentLevel = tap.currentLevel,
                totalUsage = 0L,
                singleUsage = 0L,
                logType = TAP_RESET
            )
        )
    }
}