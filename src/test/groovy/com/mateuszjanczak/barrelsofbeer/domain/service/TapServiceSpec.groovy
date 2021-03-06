package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import com.mateuszjanczak.barrelsofbeer.domain.service.DefaultTapService
import com.mateuszjanczak.barrelsofbeer.domain.service.EventService
import com.mateuszjanczak.barrelsofbeer.domain.service.TapService
import spock.lang.Specification

import static com.mateuszjanczak.barrelsofbeer.common.ContentType.GAZDA_Marcowe
import static com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_NEW
import static com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ
import static com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_READ_TEMPERATURE
import static com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_RESET
import static com.mateuszjanczak.barrelsofbeer.common.LogType.TAP_SET
import static com.mateuszjanczak.barrelsofbeer.fixtures.SensorPropertiesFixture.sensorProperties
import static com.mateuszjanczak.barrelsofbeer.fixtures.TapDetailsFixture.tapDetails
import static com.mateuszjanczak.barrelsofbeer.fixtures.TapFixture.tap

class TapServiceSpec extends Specification {

    TapRepository tapRepository
    EventService eventService

    TapService tapService

    def setup() {
        tapRepository = Mock()
        eventService = Mock()

        tapService = new DefaultTapService(tapRepository, eventService)
    }

    def "Should create tap"() {
        given:
        def tap = tap(tapId: 1, barrelContent: "No content", temperature: 0f, currentLevel: 0L, capacity: 0L, enabled: false)

        when:
        tapService.createTap(tap.tapId)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.save(tap) >> tap

        and:
        1 * eventService.saveEvent(tap, TAP_NEW)
    }

    def "Should not set a tap when it does not exist"() {
        given:
        def tapId = 1
        def tapDetails = tapDetails(contentType: GAZDA_Marcowe, capacity: 30000L)

        when:
        tapService.setTap(tapId, tapDetails)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findById(tapId) >> {
            Optional.ofNullable(null)
        }

        and:
        0 * tapRepository.save(_)

        and:
        0 * eventService.saveEvent(_, _)
    }

    def "Should not set a tap when it capacity is less than previous"() {
        given:
        def tapId = 1
        def initTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 0f, currentLevel: 25000L, capacity: 30000L, enabled: false)
        def tapDetails = tapDetails(contentType: GAZDA_Marcowe, capacity: 29999L)

        when:
        tapService.setTap(tapId, tapDetails)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findById(tapId) >> {
            Optional.of(initTap)
        }

        and:
        0 * tapRepository.save(_)

        and:
        0 * eventService.saveEvent(_, _)
    }

    def "Should set tap"() {
        given:
        def tapId = 1
        def initTap = tap(tapId: 1, barrelContent: "No content", temperature: 0f, currentLevel: 0L, capacity: 0L, enabled: false)
        def tapDetails = tapDetails(contentType: GAZDA_Marcowe, capacity: 30000L)
        def finalTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 0f, currentLevel: 30000L, capacity: 30000L, enabled: false)

        when:
        tapService.setTap(tapId, tapDetails)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findById(tapId) >> {
            Optional.of(initTap)
        }

        and:
        1 * tapRepository.save(finalTap) >> finalTap

        and:
        1 * eventService.saveEvent(finalTap, TAP_SET)
    }

    def "Should return the saved taps when getting the tap list"() {
        given:
        def tapList = [
                tap(tapId: 1, barrelContent: "Harnold"),
                tap(tapId: 2, barrelContent: "Tyskacz"),
                tap(tapId: 3, barrelContent: "Ksi???? Czarny")
        ]

        when:
        def result = tapService.getTapList()

        then:
        result == tapList

        and:
        1 * tapRepository.findAll() >> {
            tapList
        }

        and:
        0 * eventService.saveEvent(_, _)
    }

    def "Should not save sensor properties when no tap"() {
        given:
        def tapId = 1
        def sensorProperties = sensorProperties(currentLevel: 21170L, temperature: 13.9f)

        when:
        tapService.saveSensorProperties(tapId, sensorProperties)
        sleep(500)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findById(tapId) >> {
            Optional.ofNullable(null)
        }

        and:
        0 * eventService.saveEvent(_, _)
    }

    def "Should not save sensor properties when calculated current level is below 0 and automatically resize barrel"() {
        given:
        def tapId = 1
        def initTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 13.9f, currentLevel: 5000L, capacity: 30000L, enabled: true)
        def sensorProperties = sensorProperties(currentLevel: 32000, temperature: 14.9f)
        def resizedCapacityTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 13.9f, currentLevel: 35000L, capacity: 60000L, enabled: true)

        when:
        tapService.saveSensorProperties(tapId, sensorProperties)
        sleep(500)

        then:
        noExceptionThrown()

        and:
        2 * tapRepository.findById(tapId) >> {
            Optional.ofNullable(initTap)
        }

        and:
        1 * tapRepository.save(resizedCapacityTap) >> resizedCapacityTap

        and:
        1 * eventService.saveEvent(resizedCapacityTap, TAP_SET)
    }

    def "Should save sensor properties but not logging the same current level"() {
        given:
        def tapId = 1
        def initTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 13.9f, currentLevel: 5000L, capacity: 30000L, enabled: true)
        def sensorProperties = sensorProperties(currentLevel: 25000L, temperature: 14.9f)
        def finalTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 14.9f, currentLevel: 5000L, capacity: 30000L, enabled: true)

        when:
        tapService.saveSensorProperties(tapId, sensorProperties)
        sleep(500)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findById(tapId) >> {
            Optional.ofNullable(initTap)
        }

        and:
        1 * tapRepository.save(finalTap) >> finalTap

        and:
        0 * eventService.saveEvent(finalTap, TAP_READ)
        1 * eventService.saveEvent(finalTap, TAP_READ_TEMPERATURE)
    }

    def "Should save sensor properties but not logging similar temperature"() {
        given:
        def tapId = 1
        def initTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 13.9f, currentLevel: 5000L, capacity: 30000L, enabled: true)
        def sensorProperties = sensorProperties(currentLevel: 25000L, temperature: 13.8f)
        def finalTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 13.8f, currentLevel: 5000L, capacity: 30000L, enabled: true)

        when:
        tapService.saveSensorProperties(tapId, sensorProperties)
        sleep(500)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findById(tapId) >> {
            Optional.ofNullable(initTap)
        }

        and:
        1 * tapRepository.save(finalTap) >> finalTap

        and:
        0 * eventService.saveEvent(finalTap, TAP_READ)
        0 * eventService.saveEvent(finalTap, TAP_READ_TEMPERATURE)
    }

    def "Should detect counter reset and empty the barrel"() {
        given:
        def tapId = 1
        def initTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 14.5f, currentLevel: 25000L, capacity: 120000L, enabled: true)
        def sensorProperties = sensorProperties(currentLevel: 100L, temperature: 13.9f)
        def emptyBarrelTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 0.0f, currentLevel: 30000L, capacity: 30000L, enabled: true)

        when:
        tapService.saveSensorProperties(tapId, sensorProperties)
        sleep(500)

        then:
        noExceptionThrown()

        and:
        2 * tapRepository.findById(tapId) >> {
            Optional.ofNullable(initTap)
        }

        and:
        1 * tapRepository.save(emptyBarrelTap) >> emptyBarrelTap

        and:
        1 * eventService.saveEvent(emptyBarrelTap, TAP_RESET)
    }

    def "Should save sensor properties but not logging the same temperature"() {
        given:
        def tapId = 1
        def initTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 13.9f, currentLevel: 25000L, capacity: 30000L, enabled: true)
        def sensorProperties = sensorProperties(currentLevel: 12000L, temperature: 13.9f)
        def finalTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 13.9f, currentLevel: 18000L, capacity: 30000L, enabled: true)

        when:
        tapService.saveSensorProperties(tapId, sensorProperties)
        sleep(500)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findById(tapId) >> {
            Optional.ofNullable(initTap)
        }

        and:
        1 * tapRepository.save(finalTap) >> finalTap

        and:
        1 * eventService.saveEvent(initTap, finalTap, TAP_READ)
        0 * eventService.saveEvent(finalTap, TAP_READ_TEMPERATURE)
    }

    def "Should save sensor properties"() {
        given:
        def tapId = 1
        def initTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 14.5f, currentLevel: 25000L, capacity: 30000L, enabled: true)
        def sensorProperties = sensorProperties(currentLevel: 12000L, temperature: 13.9f)
        def finalTap = tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 13.9f, currentLevel: 18000L, capacity: 30000L, enabled: true)

        when:
        tapService.saveSensorProperties(tapId, sensorProperties)
        sleep(500)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findById(tapId) >> {
            Optional.ofNullable(initTap)
        }

        and:
        1 * tapRepository.save(finalTap) >> finalTap

        and:
        1 * eventService.saveEvent(initTap, finalTap, TAP_READ)
        1 * eventService.saveEvent(finalTap, TAP_READ_TEMPERATURE)
    }
}
