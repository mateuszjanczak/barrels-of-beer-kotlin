package com.mateuszjanczak.barrelsofbeer.fixtures

import com.mateuszjanczak.barrelsofbeer.infrastructure.SRData
import com.mateuszjanczak.barrelsofbeer.infrastructure.SensorResponse

class SensorResponseFixture {

    static SensorResponse sensorResponse(args = [:]) {
        new SensorResponse(
            'data' in args.keySet() ? args.data as SRData : srData()
        )
    }

    static SRData srData(args = [:]) {
        new SRData(
            'value' in args.keySet() ? args.value as String : '43BAC7AE00000204'
        )
    }
}
