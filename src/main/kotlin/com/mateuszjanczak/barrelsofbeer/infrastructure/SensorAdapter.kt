package com.mateuszjanczak.barrelsofbeer.infrastructure

import com.mateuszjanczak.barrelsofbeer.domain.Sensor
import com.mateuszjanczak.barrelsofbeer.domain.SensorData

class SensorAdapter(
    private val sensorClient: SensorClient
) : Sensor {

    override fun getSensorData(): SensorData {
        val sensorResponse = sensorClient.getSensorResponse()
        return SensorData(sensorResponse.data.value)
    }
}