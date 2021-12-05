package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.common.ContentType
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_NEW
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ_TEMPERATURE
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_RESET
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_SET
import com.mateuszjanczak.barrelsofbeer.common.TapAlreadyExistsException
import com.mateuszjanczak.barrelsofbeer.common.TapNotFoundException
import com.mateuszjanczak.barrelsofbeer.domain.SensorProperties
import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.TapDetails
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.math.abs

interface TapService {
    fun createTap(tapId: Int)
    fun setTap(tapId: Int, tapDetails: TapDetails)
    fun resetTap(tapId: Int)
    fun getTap(tapId: Int): Tap?
    fun getTapList(): List<Tap>
    fun saveSensorProperties(tapId: Int, sensorProperties: SensorProperties)
}

@Service
class DefaultTapService(
    private val tapRepository: TapRepository,
    private val eventService: EventService
) : TapService {

    companion object {
        private val log = LoggerFactory.getLogger(DefaultTapService::class.java)
    }

    override fun createTap(tapId: Int) {
        if(tapRepository.existsByTapId(tapId)) throw TapAlreadyExistsException()
        tapRepository.save(
            Tap(tapId)
        )
            .let { eventService.saveEvent(it, TAP_NEW) }
            .let { log.warn("Tap $tapId was created") }
    }

    override fun setTap(tapId: Int, tapDetails: TapDetails) {
        tapRepository.findByIdOrNull(tapId)?.let { previous ->
            val capacityIsHigherThanBefore = previous.capacity < tapDetails.capacity
            if (capacityIsHigherThanBefore) {
                val currentLevel = tapDetails.capacity - previous.capacity + previous.currentLevel
                tapRepository.save(
                    Tap(
                        tapId = tapId,
                        barrelContent = tapDetails.contentTypeAsString,
                        temperature = previous.temperature,
                        currentLevel = currentLevel,
                        capacity = tapDetails.capacity,
                        enabled = previous.enabled
                    )
                )
                    .let { next -> eventService.saveEvent(next, TAP_SET) }
                    .let { log.warn("Tap $tapId was set to the following values: $tapDetails") }
            }
        }
    }

    override fun resetTap(tapId: Int) {
        tapRepository.findByIdOrNull(tapId)?.let { previous ->
            tapRepository.save(
                Tap(
                    tapId = tapId,
                    barrelContent = previous.barrelContent,
                    currentLevel = 30000L,
                    capacity = 30000L,
                    enabled = previous.enabled
                )
            )
                .let { next -> eventService.saveEvent(next, TAP_RESET) }
                .let { log.warn("A reset of tap $tapId was detected and it was set to an empty barrel") }
        }
    }

    override fun getTap(tapId: Int): Tap? = tapRepository.findByTapId(tapId) ?: throw TapNotFoundException()

    override fun getTapList(): List<Tap> = tapRepository.findAll()

    override fun saveSensorProperties(tapId: Int, sensorProperties: SensorProperties) {
        tapRepository.findByIdOrNull(tapId)?.let { previous ->
            val currentLevel = previous.capacity - sensorProperties.currentLevel
            if (previous.currentLevel < currentLevel) {
                resetTap(1)
            } else if (currentLevel > 0) {
                tapRepository.save(
                    Tap(
                        tapId = previous.tapId,
                        barrelContent = previous.barrelContent,
                        temperature = sensorProperties.temperature,
                        currentLevel = currentLevel,
                        capacity = previous.capacity,
                        enabled = previous.enabled
                    )
                )
                    .let { next ->
                        if (previous.currentLevel != currentLevel)
                            eventService.saveEvent(previous, next, TAP_READ)
                        if (abs(previous.temperature - next.temperature) >= 0.5)
                            eventService.saveEvent(next, TAP_READ_TEMPERATURE)
                    }
                    .let { log.warn("Tap $tapId has been updated with the following values: $sensorProperties") }
            } else {
                setTap(tapId, TapDetails(ContentType.valueOf(previous.barrelContent.replace(' ', '_')), previous.capacity + 30000L))
            }
        }
    }
}