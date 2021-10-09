package com.mateuszjanczak.barrelsofbeer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BarrelsOfBeerApplication

fun main(args: Array<String>) {
    runApplication<BarrelsOfBeerApplication>(*args)
}
