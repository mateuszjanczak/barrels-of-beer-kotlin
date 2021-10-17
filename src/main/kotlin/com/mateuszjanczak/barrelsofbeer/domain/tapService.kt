package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.TapDetails
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface TapService {
    fun createTap(tapId: Int)
    fun setTap(tapId: Int, tapDetails: TapDetails)
    fun getTap(tapId: Int): Tap?
    fun getTapList(): List<Tap>
    fun saveSensorProperties(tapId: Int, sensorProperties: SensorProperties)
}

@Service
class DefaultTapService(
    private val tapRepository: TapRepository
) : TapService {

    override fun createTap(tapId: Int) {
        tapRepository.save(
            Tap(tapId)
        )
    }

    override fun setTap(tapId: Int, tapDetails: TapDetails) {
        tapRepository.findByIdOrNull(tapId)?.let {
            val capacityIsHigherThanBefore = it.capacity < tapDetails.capacity
            if (capacityIsHigherThanBefore) {
                val currentLevel = tapDetails.capacity - it.capacity + it.currentLevel
                tapRepository.save(
                    Tap(
                        tapId = tapId,
                        barrelContent = tapDetails.contentType,
                        currentLevel = currentLevel,
                        capacity = tapDetails.capacity
                    )
                )
            }
        }
    }

    override fun getTap(tapId: Int): Tap? = tapRepository.findByIdOrNull(tapId)

    override fun getTapList(): List<Tap> = tapRepository.findAll()

    override fun saveSensorProperties(tapId: Int, sensorProperties: SensorProperties) {
        tapRepository.findByIdOrNull(tapId)?.let {
            val currentLevel = it.capacity - sensorProperties.currentLevel
            tapRepository.save(
                Tap(
                    tapId = it.tapId,
                    barrelContent = it.barrelContent,
                    temperature = sensorProperties.temperature,
                    currentLevel = currentLevel,
                    capacity = it.capacity,
                    enabled = it.enabled
                )
            )
        }
    }
}