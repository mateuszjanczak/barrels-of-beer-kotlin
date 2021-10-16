package com.mateuszjanczak.barrelsofbeer.fixtures

import com.mateuszjanczak.barrelsofbeer.domain.SensorProperties

class SensorPropertiesFixture {

    static SensorProperties sensorProperties(args = [:]) {
        new SensorProperties(
            'currentLevel' in args.keySet() ? args.currentLevel as Long : 1000L,
            'temperature' in args.keySet() ? args.temperature as Float : 15.6f
        )
    }
}
