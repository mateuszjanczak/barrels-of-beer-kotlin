package com.mateuszjanczak.barrelsofbeer.infrastructure

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.mateuszjanczak.barrelsofbeer.common.SensorHttpClientException
import com.mateuszjanczak.barrelsofbeer.configuration.SensorConfiguration
import org.springframework.stereotype.Component

interface SensorClient {
    fun getSensorResponse(id: Int): SensorResponse
}

@Component
class DefaultSensorClient(
    private val sensorConfiguration: SensorConfiguration,
    private val objectMapper: ObjectMapper
) : SensorClient {

    override fun getSensorResponse(id: Int): SensorResponse {
        val result = try {
            khttp.get(url = sensorConfiguration.baseUrl.fillSensorId(id), timeout = sensorConfiguration.timeout)
        } catch (e: Exception) {
            throw SensorHttpClientException("HttpClientException", e)
        }

        return try {
            objectMapper.readValue(result.text, SensorResponse::class.java)
        } catch (e: JsonProcessingException) {
            throw SensorHttpClientException("JsonProcessingException", e)
        }
    }

    private fun String.fillSensorId(id: Int): String = this.replace("SENSOR_ID", "$id")
}
