package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface TapService {
    fun getTap(tapId: Int): Tap?
    fun getTapList(): List<Tap>
    fun saveSensorProperties(tapId: Int, sensorProperties: SensorProperties)
}

@Service
class DefaultTapService(
    private val tapRepository: TapRepository
) : TapService {

    override fun getTap(tapId: Int): Tap? = tapRepository.findByIdOrNull(tapId)

    override fun getTapList(): List<Tap> = tapRepository.findAll()

    override fun saveSensorProperties(tapId: Int, sensorProperties: SensorProperties) {
        tapRepository.findByIdOrNull(tapId)?.let {
            tapRepository.save(
                Tap(
                    tapId = it.tapId,
                    barrelContent = it.barrelContent,
                    temperature = sensorProperties.temperature,
                    currentLevel = calculateCurrentLevel(it.capacity, sensorProperties.currentLevel),
                    capacity = it.capacity,
                    enabled = it.enabled
                )
            )
        }
    }

    private fun calculateCurrentLevel(capacity: Long, currentLevel: Long): Long = capacity - currentLevel
}