package com.mateuszjanczak.barrelsofbeer.fixtures

import com.mateuszjanczak.barrelsofbeer.domain.SensorData

class SensorDataFixture {

    static SensorData sensorData(args = [:]) {
        new SensorData(
            'value' in args.keySet() ? args.value as String : 'FFFFFF'
        )
    }
}