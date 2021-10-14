package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.common.SensorHttpClientException
import com.mateuszjanczak.barrelsofbeer.infrastructure.SensorAdapter
import com.mateuszjanczak.barrelsofbeer.infrastructure.SensorClient
import spock.lang.Specification

import static com.mateuszjanczak.barrelsofbeer.fixtures.SensorDataFixture.sensorData
import static com.mateuszjanczak.barrelsofbeer.fixtures.SensorResponseFixture.sensorResponse
import static com.mateuszjanczak.barrelsofbeer.fixtures.SensorResponseFixture.srData

class SensorSpec extends Specification {

    SensorClient client
    Sensor sensor

    def setup() {
        client = Mock()
        sensor = new SensorAdapter(client)
    }

    def "Should throw an exception when getting sensor data returns error"() {
        when:
        sensor.getSensorData(1)

        then:
        thrown(SensorHttpClientException)

        and:
        1 * client.getSensorResponse(1) >> {
            throw new SensorHttpClientException("SensorHttpClientException")
        }
    }

    def "Should return sensor data"() {
        when:
        def result = sensor.getSensorData(1)

        then:
        result == sensorData(
            value: 's0m3th1ng'
        )

        and:
        1 * client.getSensorResponse(1) >> {
            sensorResponse(data: srData(value: 's0m3th1ng'))
        }

    }
}
