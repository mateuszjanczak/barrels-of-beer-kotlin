package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.IntegrationTestBase
import com.mateuszjanczak.barrelsofbeer.common.SensorHttpClientException
import com.mateuszjanczak.barrelsofbeer.infrastructure.DefaultSensorClient
import com.mateuszjanczak.barrelsofbeer.infrastructure.SensorAdapter
import spock.lang.Unroll

import static com.mateuszjanczak.barrelsofbeer.common.SerializationKt.objectMapper
import static com.mateuszjanczak.barrelsofbeer.fixtures.SensorDataFixture.sensorData

class ITestSensor extends IntegrationTestBase {

    Sensor sensor

    def setup() {
        sensor = new SensorAdapter(new DefaultSensorClient(sensorConfiguration, objectMapper))
    }

    def "Should throw an exception when getting sensor data times out"() {
        given:
        sensorMock.getSensorDataSuccessWith(timeout: 1000)

        when:
        sensor.getSensorData(1)

        then:
        thrown(SensorHttpClientException)
    }

    @Unroll
    def "Should throw an exception when getting sensor data returns http status #statusCode"() {
        given:
        sensorMock.getSensorDataFailWith(statusCode: statusCode)

        when:
        sensor.getSensorData(1)

        then:
        thrown(SensorHttpClientException)

        where:
        statusCode << [400, 500]
    }

    def "Should throw an exception when getting sensor data returns invalid response"() {
        given:
        sensorMock.getSensorDataFailWithInvalidResponse()

        when:
        sensor.getSensorData(1)

        then:
        thrown(SensorHttpClientException)
    }

    def "Should return sensor data"() {
        given:
        sensorMock.getSensorDataSuccessWith(id: 1)

        when:
        def result = sensor.getSensorData(1)

        then:
        result == sensorData(value: "43BAC7AE00000204")
    }
}
