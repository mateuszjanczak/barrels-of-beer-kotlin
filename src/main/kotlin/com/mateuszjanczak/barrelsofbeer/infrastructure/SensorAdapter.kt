package com.mateuszjanczak.barrelsofbeer.infrastructure

import com.mateuszjanczak.barrelsofbeer.domain.Sensor
import com.mateuszjanczak.barrelsofbeer.domain.SensorData
import org.springframework.stereotype.Component

@Component
class SensorAdapter(
    private val sensorClient: SensorClient
) : Sensor {

    override fun getSensorData(id: Int): SensorData {
        val sensorResponse = sensorClient.getSensorResponse(id)
        return SensorData(sensorResponse.data.value)
    }
}