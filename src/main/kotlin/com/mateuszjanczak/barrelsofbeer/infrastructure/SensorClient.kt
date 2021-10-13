package com.mateuszjanczak.barrelsofbeer.infrastructure

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.mateuszjanczak.barrelsofbeer.common.SensorHttpClientException
import com.mateuszjanczak.barrelsofbeer.configuration.SensorConfiguration

interface SensorClient {
    fun getSensorResponse(): SensorResponse
}

class DefaultSensorClient(
    private val sensorConfiguration: SensorConfiguration,
    private val objectMapper: ObjectMapper
) : SensorClient {

    override fun getSensorResponse(): SensorResponse {
        val result = try {
            khttp.get(url = sensorConfiguration.baseUrl + "/hello", timeout = sensorConfiguration.timeout)
        } catch (e: Exception) {
            throw SensorHttpClientException("HttpClientException", e)
        }

        val sensorResponse = try {
            objectMapper.readValue(result.text, SensorResponse::class.java)
        } catch (e: JsonProcessingException) {
            throw SensorHttpClientException("JsonProcessingException", e)
        }

        return sensorResponse
    }
}
