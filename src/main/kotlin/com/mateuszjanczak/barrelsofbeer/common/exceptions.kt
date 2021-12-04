package com.mateuszjanczak.barrelsofbeer.common

class SensorHttpClientException @JvmOverloads constructor(
    override val message: String?,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)

class TapNotFoundException : RuntimeException("TapNotFoundException")