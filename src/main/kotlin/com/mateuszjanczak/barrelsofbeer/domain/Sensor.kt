package com.mateuszjanczak.barrelsofbeer.domain

interface Sensor {
    fun getSensorData(): SensorData
}