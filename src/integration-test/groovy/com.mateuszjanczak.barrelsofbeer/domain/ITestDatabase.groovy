package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.BarrelsOfBeerApplication
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static com.mateuszjanczak.barrelsofbeer.fixtures.TapFixture.tap

@SpringBootTest(classes = BarrelsOfBeerApplication)
class ITestDatabase extends Specification {

    @Autowired
    TapRepository tapRepository

    TapService tapService

    def setup() {
        tapService = new DefaultTapService(tapRepository)
    }

    def "Should return the saved taps when getting the tap list"() {
        given:
        def tapList = [
            tap(tapId: 1, barrelContent: "Harnold"),
            tap(tapId: 2, barrelContent: "Tyskacz"),
            tap(tapId: 3, barrelContent: "Książ Czarny")
        ]
        tapRepository.saveAll(tapList)

        when:
        def result = tapService.getTapList()

        then:
        result == tapList
    }
}
