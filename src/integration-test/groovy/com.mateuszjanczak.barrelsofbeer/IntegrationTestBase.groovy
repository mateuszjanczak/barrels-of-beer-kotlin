package com.mateuszjanczak.barrelsofbeer

import com.mateuszjanczak.barrelsofbeer.configuration.SensorConfiguration
import com.mateuszjanczak.barrelsofbeer.mock.SensorMock
import spock.lang.Shared
import spock.lang.Specification

import static com.mateuszjanczak.barrelsofbeer.configuration.WireMock.wireMockServer

abstract class IntegrationTestBase extends Specification {

    @Shared
    SensorMock sensorMock
    @Shared
    SensorConfiguration sensorConfiguration

    def setupSpec() {
        sensorMock = new SensorMock(wireMockServer)
        wireMockServer.start()
        sensorConfiguration = new SensorConfiguration(buildUrl(wireMockServer.baseUrl()), 1)
    }

    def cleanup() {
        wireMockServer.resetAll()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    static templateUrl() {
        "/iolinkmaster/port[SENSOR_ID]/iolinkdevice/pdin/getdata"
    }

    private static buildUrl(baseUrl) {
        baseUrl + templateUrl()
    }
}
