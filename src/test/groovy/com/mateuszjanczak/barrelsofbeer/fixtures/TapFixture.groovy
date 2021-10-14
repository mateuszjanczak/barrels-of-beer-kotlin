package com.mateuszjanczak.barrelsofbeer.fixtures

import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap

class TapFixture {

    static Tap tap(args = [:]) {
        new Tap(
            'tapId' in args.keySet() ? args.tapId as int : 1,
            'barrelContent' in args.keySet() ? args.barrelContent as String : "Harnold",
            'temperature' in args.keySet() ? args.temperature as float : 5f,
            'currentLevel' in args.keySet() ? args.currentLevel as long : 10L,
            'capacity' in args.keySet() ? args.capacity as long : 20L,
            'enabled' in args.keySet() ? args.enabled as boolean : true
        )
    }
}
