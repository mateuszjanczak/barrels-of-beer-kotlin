package com.mateuszjanczak.barrelsofbeer.common

enum class LogType {
    TAP_NEW, TAP_SET, TAP_REMOVE, TAP_READ, TAP_READ_TEMPERATURE, TAP_ENABLE, TAP_DISABLE, TAP_RESET
}

enum class ContentType {
    CHMYZ_Pils, GAZDA_Marcowe, KRASA_Weizen, UPIR_Dunkel, KICARZ_Koźlak, KADUK_Podwójny_Koźlak, SĘDEK_IPA
}

enum class TableType {
    TAPS, ACTION_EVENTS, TEMPERATURE_EVENTS
}