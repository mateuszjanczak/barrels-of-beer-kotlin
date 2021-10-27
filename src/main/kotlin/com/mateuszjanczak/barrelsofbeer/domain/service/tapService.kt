package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.common.LogType.*
import com.mateuszjanczak.barrelsofbeer.domain.SensorProperties
import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.TapDetails
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.math.abs

interface TapService {
    fun createTap(tapId: Int)
    fun setTap(tapId: Int, tapDetails: TapDetails)
    fun getTap(tapId: Int): Tap?
    fun getTapList(): List<Tap>
    fun saveSensorProperties(tapId: Int, sensorProperties: SensorProperties)
}

@Service
class DefaultTapService(
    private val tapRepository: TapRepository,
    private val eventService: EventService
) : TapService {

    override fun createTap(tapId: Int) {
        tapRepository.save(
            Tap(tapId)
        ).let { eventService.saveEvent(it, TAP_NEW) }
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
                        currentLevel = currentLevel,
                        capacity = tapDetails.capacity
                    )
                ).let { next -> eventService.saveEvent(next, TAP_SET) }
            }
        }
    }

    override fun getTap(tapId: Int): Tap? = tapRepository.findByIdOrNull(tapId)

    override fun getTapList(): List<Tap> = tapRepository.findAll()

    override fun saveSensorProperties(tapId: Int, sensorProperties: SensorProperties) {
        tapRepository.findByIdOrNull(tapId)?.let { previous ->
            val currentLevel = previous.capacity - sensorProperties.currentLevel
            if(currentLevel > 0) {
                tapRepository.save(
                    Tap(
                        tapId = previous.tapId,
                        barrelContent = previous.barrelContent,
                        temperature = sensorProperties.temperature,
                        currentLevel = currentLevel,
                        capacity = previous.capacity,
                        enabled = previous.enabled
                    )
                ).let { next ->
                    if (previous.currentLevel != currentLevel)
                        eventService.saveEvent(previous, next, TAP_READ)
                    if (abs(previous.temperature - next.temperature) >= 0.5)
                        eventService.saveEvent(next, TAP_READ_TEMPERATURE)
                }
            }
        }
    }
}