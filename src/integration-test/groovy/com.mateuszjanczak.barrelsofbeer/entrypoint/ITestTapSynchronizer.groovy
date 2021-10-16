package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.BarrelsOfBeerApplication
import com.mateuszjanczak.barrelsofbeer.IntegrationTestBase
import com.mateuszjanczak.barrelsofbeer.domain.DefaultSensorService
import com.mateuszjanczak.barrelsofbeer.domain.TapService
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import com.mateuszjanczak.barrelsofbeer.infrastructure.DefaultSensorClient
import com.mateuszjanczak.barrelsofbeer.infrastructure.SensorAdapter
import com.mateuszjanczak.barrelsofbeer.utils.SensorExtractor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import static com.mateuszjanczak.barrelsofbeer.common.SerializationKt.objectMapper
import static com.mateuszjanczak.barrelsofbeer.fixtures.TapFixture.tap

@SpringBootTest(classes = BarrelsOfBeerApplication)
class ITestTapSynchronizer extends IntegrationTestBase {

    @Autowired
    TapRepository tapRepository

    @Autowired
    TapService tapService

    TapSynchronizer tapSynchronizer

    def setup() {
        def sensor = new SensorAdapter(new DefaultSensorClient(sensorConfiguration, objectMapper))
        def sensorExtractor = new SensorExtractor()
        def sensorService = new DefaultSensorService(sensor, sensorExtractor)

        tapSynchronizer = new TapSynchronizer(sensorService, tapService)
    }

    def "Should not synchronize taps with sensors when no taps"() {
        when:
        tapSynchronizer.synchronizeTaps()
        sleep(1000)

        then:
        noExceptionThrown()

        and:
        tapService.getTapList() == []
    }

    def "Should not synchronize taps with sensors when throw SensorHttpClientException"() {
        given:
        def tapList = [
            tap(tapId: 1)
        ]
        tapRepository.saveAll(tapList)

        and:
        sensorMock.getSensorDataFailWith(id: 1)

        when:
        tapSynchronizer.synchronizeTaps()
        sleep(1000)

        then:
        noExceptionThrown()

        and:
        tapService.getTapList() >> [
            tap(tapId: 1)
        ]
    }

    def "Should synchronize taps with sensors"() {
        given:
        def tapList = [
            tap(tapId: 1),
            tap(tapId: 2),
            tap(tapId: 3)
        ]
        tapRepository.saveAll(tapList)

        and:
        sensorMock.getSensorDataSuccessWith(id: 1, hex: "3F73333300000278")
        sensorMock.getSensorDataSuccessWith(id: 2, hex: "3FC7AE1400000230")
        sensorMock.getSensorDataSuccessWith(id: 3, hex: "41A95C290000022C")

        when:
        tapSynchronizer.synchronizeTaps()
        sleep(1000)

        then:
        noExceptionThrown()

        and:
        tapService.getTapList() == [
            tap(tapId: 1, currentLevel: 950L, temperature: 15.8f),
            tap(tapId: 2, currentLevel: 1560L, temperature: 14.0f),
            tap(tapId: 3, currentLevel: 21170L, temperature: 13.9f),
        ]
    }
}
