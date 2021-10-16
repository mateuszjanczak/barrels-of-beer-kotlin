package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import spock.lang.Specification

import static com.mateuszjanczak.barrelsofbeer.fixtures.TapFixture.tap

class TapServiceSpec extends Specification {

    TapRepository tapRepository
    TapService tapService

    def setup() {
        tapRepository = Mock()
        tapService = new DefaultTapService(tapRepository)
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
