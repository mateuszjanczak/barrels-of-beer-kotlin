package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_DISABLE
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_ENABLE
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_REMOVE
import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface AdminService {
    fun toggleTap(tapId: Int, enabled: Boolean)
    fun removeTap(tapId: Int)
}

@Service
class DefaultAdminService(
    private val tapRepository: TapRepository,
    private val eventService: EventService
) : AdminService {

    override fun toggleTap(tapId: Int, enabled: Boolean) {
        tapRepository.findByIdOrNull(tapId)?.let { previous ->
            tapRepository.save(
                Tap(
                    tapId = previous.tapId,
                    barrelContent = previous.barrelContent,
                    temperature = previous.temperature,
                    currentLevel = previous.currentLevel,
                    capacity = previous.capacity,
                    enabled = enabled
                )
            ).let { next ->
                eventService.saveEvent(next, if (enabled) TAP_ENABLE else TAP_DISABLE)
            }
        }
    }

    override fun removeTap(tapId: Int) {
        tapRepository.findByIdOrNull(tapId)?.let { previous ->
            tapRepository.deleteById(tapId).let {
                eventService.saveEvent(previous, TAP_REMOVE)
            }
        }
    }
}