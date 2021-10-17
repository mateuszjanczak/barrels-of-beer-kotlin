package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import spock.lang.Specification

import static com.mateuszjanczak.barrelsofbeer.common.ContentType.GAZDA_Marcowe
import static com.mateuszjanczak.barrelsofbeer.fixtures.TapDetailsFixture.tapDetails
import static com.mateuszjanczak.barrelsofbeer.fixtures.TapFixture.tap

class TapServiceSpec extends Specification {

    TapRepository tapRepository
    TapService tapService

    def setup() {
        tapRepository = Mock()
        tapService = new DefaultTapService(tapRepository)
    }

    def "Should create tap"() {
        given:
        def tap = tap(tapId: 1, barrelContent: "No content", temperature: 0f, currentLevel: 0L, capacity: 0L, enabled: false)

        when:
        tapService.createTap(tap.tapId)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.save(tap)
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
    }

    def "Should set tap"() {
        given:
        def tapId = 1
        def initTap = tap(tapId: 1, barrelContent: "No content", temperature: 0f, currentLevel: 0L, capacity: 0L, enabled: false)
        def tapDetails = tapDetails(contentType: GAZDA_Marcowe, capacity: 30000L)

        when:
        tapService.setTap(tapId, tapDetails)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findById(tapId) >> {
            Optional.of(initTap)
        }

        and:
        1 * tapRepository.save(
            tap(tapId: 1, barrelContent: "GAZDA Marcowe", temperature: 0f, currentLevel: 30000L, capacity: 30000L, enabled: false)
        )
    }

    def "Should return the saved taps when getting the tap list"() {
        given:
        def tapList = [
            tap(tapId: 1, barrelContent: "Harnold"),
            tap(tapId: 2, barrelContent: "Tyskacz"),
            tap(tapId: 3, barrelContent: "Książ Czarny")
        ]

        when:
        def result = tapService.getTapList()

        then:
        result == tapList

        and:
        1 * tapRepository.findAll() >> {
            tapList
        }
    }
}
