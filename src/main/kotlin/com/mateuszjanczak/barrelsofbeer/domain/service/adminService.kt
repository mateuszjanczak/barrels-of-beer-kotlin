package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_DISABLE
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_ENABLE
import com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_REMOVE
import com.mateuszjanczak.barrelsofbeer.common.TableType
import com.mateuszjanczak.barrelsofbeer.common.TableType.ACTION_EVENTS
import com.mateuszjanczak.barrelsofbeer.common.TableType.TAPS
import com.mateuszjanczak.barrelsofbeer.common.TableType.TEMPERATURE_EVENTS
import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.ActionEventRepository
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TemperatureEventRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface AdminService {
    fun toggleTap(tapId: Int, enabled: Boolean)
    fun removeTap(tapId: Int)
    fun resetDatabase(tableType: TableType)
}

@Service
class DefaultAdminService(
    private val tapRepository: TapRepository,
    private val actionEventRepository: ActionEventRepository,
    private val temperatureEventRepository: TemperatureEventRepository,
    private val eventService: EventService
) : AdminService {

    companion object {
        private val log = LoggerFactory.getLogger(DefaultAdminService::class.java)
    }

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
                log.warn("Tap $tapId status changed to {}", if (enabled) TAP_ENABLE else TAP_DISABLE)
            }
        }
    }

    override fun removeTap(tapId: Int) {
        tapRepository.findByIdOrNull(tapId)?.let { previous ->
            tapRepository.deleteById(tapId).let {
                eventService.saveEvent(previous, TAP_REMOVE)
                    .also { log.warn("Tap $tapId has been removed") }
            }
        }
    }

    override fun resetDatabase(tableType: TableType) =
        when (tableType) {
            TAPS -> tapRepository.deleteAll()
            ACTION_EVENTS -> actionEventRepository.deleteAll()
            TEMPERATURE_EVENTS -> temperatureEventRepository.deleteAll()
        }.also { log.warn("Table $tableType has been removed") }

}