package com.mateuszjanczak.barrelsofbeer.fixtures

import com.mateuszjanczak.barrelsofbeer.common.ContentType
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.TapDetails

class TapDetailsFixture {

    static TapDetails tapDetails(args = [:]) {
        new TapDetails(
            'contentType' in args.keySet() ? args.contentType as ContentType : ContentType.GAZDA_Marcowe,
            'capacity' in args.keySet() ? args.capacity as Long : 30000L
        )
    }
}
