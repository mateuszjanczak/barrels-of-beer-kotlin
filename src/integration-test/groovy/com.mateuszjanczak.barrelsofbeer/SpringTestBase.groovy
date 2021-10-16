package com.mateuszjanczak.barrelsofbeer

import com.mateuszjanczak.barrelsofbeer.domain.data.repository.ActionEventRepository
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TemperatureEventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = BarrelsOfBeerApplication)
class SpringTestBase extends IntegrationTestBase {

    @Autowired
    TapRepository tapRepository

    @Autowired
    ActionEventRepository actionEventRepository

    @Autowired
    TemperatureEventRepository temperatureEventRepository

    def cleanup() {
        tapRepository.deleteAll()
        actionEventRepository.deleteAll()
        temperatureEventRepository.deleteAll()
    }
}
