package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.domain.Sensor
import com.mateuszjanczak.barrelsofbeer.domain.SensorProperties
import com.mateuszjanczak.barrelsofbeer.utils.SensorExtractor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

interface SensorService {
    fun getSensorProperties(id: Int): SensorProperties?
}

@Service
class DefaultSensorService(
    private val sensor: Sensor,
    private val sensorExtractor: SensorExtractor
) : SensorService {

    companion object {
        private val log = LoggerFactory.getLogger(DefaultSensorService::class.java)
    }

    override fun getSensorProperties(id: Int): SensorProperties? = try {
        sensorExtractor.getSensorPropertiesFromSensorData(sensor.getSensorData(id))
    } catch (e: Exception) {
        log.error("Failed to get sensor properties from tap $id", e)
        null
    }
}