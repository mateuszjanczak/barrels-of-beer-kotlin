package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.BarrelsOfBeerApplication
import com.mateuszjanczak.barrelsofbeer.SpringTestBase
import com.mateuszjanczak.barrelsofbeer.domain.service.DefaultSensorService
import com.mateuszjanczak.barrelsofbeer.domain.service.TapService
import com.mateuszjanczak.barrelsofbeer.infrastructure.DefaultSensorClient
import com.mateuszjanczak.barrelsofbeer.infrastructure.SensorAdapter
import com.mateuszjanczak.barrelsofbeer.utils.SensorExtractor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import static com.mateuszjanczak.barrelsofbeer.common.SerializationKt.objectMapper
import static com.mateuszjanczak.barrelsofbeer.fixtures.TapFixture.tap

@SpringBootTest(classes = BarrelsOfBeerApplication)
class ITestTapSynchronizer extends SpringTestBase {

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

    def "Should synchronize taps even if one fails and seconds disabled"() {
        given:
        def tapList = [
            tap(tapId: 1),
            tap(tapId: 2),
            tap(tapId: 3),
            tap(tapId: 4, enabled: false)
        ]
        tapRepository.saveAll(tapList)

        and:
        sensorMock.getSensorDataSuccessWith(id: 1, hex: "3F73333300000278")
        sensorMock.getSensorDataFailWith(id: 2)
        sensorMock.getSensorDataSuccessWith(id: 3, hex: "41A95C290000022C")

        when:
        tapSynchronizer.synchronizeTaps()
        sleep(1000)

        then:
        noExceptionThrown()

        and:
        tapService.getTapList() == [
            tap(tapId: 1, currentLevel: 29050L, temperature: 15.8f),
            tap(tapId: 2, currentLevel: 20000L, temperature: 5.0f),
            tap(tapId: 3, currentLevel: 8830L, temperature: 13.9f),
            tap(tapId: 4, currentLevel: 20000L, temperature: 5.0f, enabled: false)
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
            tap(tapId: 1, currentLevel: 29050L, temperature: 15.8f),
            tap(tapId: 2, currentLevel: 28440L, temperature: 14.0f),
            tap(tapId: 3, currentLevel: 8830L, temperature: 13.9f),
        ]
    }
}
