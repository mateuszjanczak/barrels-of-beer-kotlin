package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.common.SensorHttpClientException
import com.mateuszjanczak.barrelsofbeer.domain.service.SensorService
import com.mateuszjanczak.barrelsofbeer.domain.service.TapService
import spock.lang.Specification

import static com.mateuszjanczak.barrelsofbeer.fixtures.SensorPropertiesFixture.sensorProperties
import static com.mateuszjanczak.barrelsofbeer.fixtures.TapFixture.tap

class TapSynchronizerSpec extends Specification {

    SensorService sensorService
    TapService tapService

    TapSynchronizer tapSynchronizer

    def setup() {
        sensorService = Mock()
        tapService = Mock()

        tapSynchronizer = new TapSynchronizer(sensorService, tapService)
    }

    def "Should not synchronize taps with sensors when no taps"() {
        given:
        def tapList = []

        when:
        tapSynchronizer.synchronizeTaps()
        sleep(100)

        then:
        noExceptionThrown()

        and:
        1 * tapService.getTapList() >> tapList

        and:
        0 * sensorService.getSensorProperties(_)
    }

    def "Should not synchronize taps with sensors when throw SensorHttpClientException"() {
        given:
        def tapList = [
            tap(tapId: 1)
        ]

        when:
        tapSynchronizer.synchronizeTaps()
        sleep(100)

        then:
        noExceptionThrown()

        and:
        1 * tapService.getTapList() >> tapList

        and:
        1 * sensorService.getSensorProperties(1) >> {
            throw new SensorHttpClientException("SensorHttpClientException")
        }

        and:
        0 * tapService.saveSensorProperties(1, _)
    }

    def "Should synchronize taps even if one fails and seconds disabled"() {
        given:
        def tapList = [
            tap(tapId: 1),
            tap(tapId: 2),
            tap(tapId: 3),
            tap(tapId: 4, enabled: false)
        ]

        when:
        tapSynchronizer.synchronizeTaps()
        sleep(100)

        then:
        noExceptionThrown()

        and:
        1 * tapService.getTapList() >> tapList

        and:
        1 * sensorService.getSensorProperties(1) >> sensorProperties()
        1 * sensorService.getSensorProperties(2) >> sensorProperties()
        1 * sensorService.getSensorProperties(3) >> {
            throw new SensorHttpClientException("SensorHttpClientException")
        }
        0 * sensorService.getSensorProperties(4)

        and:
        1 * tapService.saveSensorProperties(1, sensorProperties())
        1 * tapService.saveSensorProperties(2, sensorProperties())
        0 * tapService.saveSensorProperties(3, sensorProperties())
        0 * tapService.saveSensorProperties(4, sensorProperties())
    }

    def "Should synchronize taps with sensors"() {
        given:
        def tapList = [
            tap(tapId: 1),
            tap(tapId: 2)
        ]

        when:
        tapSynchronizer.synchronizeTaps()
        sleep(100)

        then:
        noExceptionThrown()

        and:
        1 * tapService.getTapList() >> tapList

        and:
        1 * sensorService.getSensorProperties(1) >> sensorProperties()
        1 * sensorService.getSensorProperties(2) >> sensorProperties()

        and:
        1 * tapService.saveSensorProperties(1, sensorProperties())
        1 * tapService.saveSensorProperties(2, sensorProperties())
    }
}
