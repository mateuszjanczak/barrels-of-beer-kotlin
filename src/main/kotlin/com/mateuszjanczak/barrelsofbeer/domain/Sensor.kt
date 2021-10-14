package com.mateuszjanczak.barrelsofbeer.domain

interface Sensor {
    fun getSensorData(id: Int): SensorData
}