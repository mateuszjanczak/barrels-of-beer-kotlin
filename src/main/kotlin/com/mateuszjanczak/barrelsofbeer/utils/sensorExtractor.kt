package com.mateuszjanczak.barrelsofbeer.utils

import com.mateuszjanczak.barrelsofbeer.domain.SensorData
import com.mateuszjanczak.barrelsofbeer.domain.SensorProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

typealias Hex = String
typealias Bin = String

@Component
class SensorExtractor {

    companion object {
        private val log = LoggerFactory.getLogger(SensorExtractor::class.java)
    }

    fun getSensorPropertiesFromSensorData(sensorData: SensorData): SensorProperties {
        val hex = sensorData.value
        val bin = hexToBin(hex)

        val currentLevel = getCurrentLevel(bin.currentLevel())
        val temperature = getTemperature(bin.temperature())

        log.debug("Hex: $hex Bin: $bin CurrentLevel: $currentLevel Temperature: $temperature")

        return SensorProperties(currentLevel, temperature)
    }

    private fun Bin.currentLevel() = this.substring(0, 32)

    private fun Bin.temperature() = this.substring(48, 62)

    private fun getCurrentLevel(bin: Bin): Long = (Float.fromBits(bin.toInt(2)) * 1000L).toLong()

    private fun getTemperature(bin: Bin): Float = bin.toInt(2) / 10.0f

    private fun hexToBin(value: Hex): Bin {
        var hex = value
        hex = hex.replace("0", "0000")
        hex = hex.replace("1", "0001")
        hex = hex.replace("2", "0010")
        hex = hex.replace("3", "0011")
        hex = hex.replace("4", "0100")
        hex = hex.replace("5", "0101")
        hex = hex.replace("6", "0110")
        hex = hex.replace("7", "0111")
        hex = hex.replace("8", "1000")
        hex = hex.replace("9", "1001")
        hex = hex.replace("A", "1010")
        hex = hex.replace("B", "1011")
        hex = hex.replace("C", "1100")
        hex = hex.replace("D", "1101")
        hex = hex.replace("E", "1110")
        hex = hex.replace("F", "1111")
        return hex
    }
}
