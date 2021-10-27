package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.domain.service.SensorService
import com.mateuszjanczak.barrelsofbeer.domain.service.TapService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TapSynchronizer(
    val sensorService: SensorService,
    val tapService: TapService
) {

    @Scheduled(fixedRate = 1000)
    fun synchronizeTaps() {
        tapService.getTapList().filter { it.enabled }
            .forEach { synchronizeTap(it.tapId) }
    }

    private fun synchronizeTap(tapId: Int) =
        Thread {
            sensorService.getSensorProperties(tapId)?.let { sensorProperties ->
                tapService.saveSensorProperties(tapId, sensorProperties)
            }
        }.start()

}