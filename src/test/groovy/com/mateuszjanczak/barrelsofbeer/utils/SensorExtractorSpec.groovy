package com.mateuszjanczak.barrelsofbeer.utils

import spock.lang.Specification
import spock.lang.Unroll

import static com.mateuszjanczak.barrelsofbeer.fixtures.SensorDataFixture.sensorData
import static com.mateuszjanczak.barrelsofbeer.fixtures.SensorPropertiesFixture.sensorProperties

class SensorExtractorSpec extends Specification {

    SensorExtractor sensorExtractor

    def setup() {
        sensorExtractor = new SensorExtractor()
    }

    @Unroll
    def "Should return sensor properties from passed sensor data"() {
        given:
        def input = sensorData

        when:
        def output = sensorExtractor.getSensorPropertiesFromSensorData(input)

        then:
        output == sensorProperties

        where:
        sensorData                            | sensorProperties
        sensorData(value: "41A95C290000022C") | sensorProperties(currentLevel: 21170L, temperature: 13.9f)
        sensorData(value: "435647AE000001FC") | sensorProperties(currentLevel: 214280L, temperature: 12.7f)
        sensorData(value: "3F73333300000278") | sensorProperties(currentLevel: 950L, temperature: 15.8f)
        sensorData(value: "3FC7AE1400000230") | sensorProperties(currentLevel: 1560L, temperature: 14.0f)
    }
}
