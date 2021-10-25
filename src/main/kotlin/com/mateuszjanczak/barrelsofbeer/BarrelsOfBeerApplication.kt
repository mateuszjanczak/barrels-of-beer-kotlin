package com.mateuszjanczak.barrelsofbeer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BarrelsOfBeerApplication

fun main(args: Array<String>) {
    runApplication<BarrelsOfBeerApplication>(*args)
}
