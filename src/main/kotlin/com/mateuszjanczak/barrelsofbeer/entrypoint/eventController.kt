package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.domain.EventService
import com.mateuszjanczak.barrelsofbeer.domain.data.document.ActionEvent
import com.mateuszjanczak.barrelsofbeer.domain.data.document.TemperatureEvent
import com.mateuszjanczak.barrelsofbeer.entrypoint.EventEndpoints.EVENTS_ACTION
import com.mateuszjanczak.barrelsofbeer.entrypoint.EventEndpoints.EVENTS_TEMPERATURE
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api"])
class EventController(
    private val eventService: EventService
) {

    @GetMapping(EVENTS_ACTION)
    fun getActionEvents(@PathVariable page: Int): ResponseEntity<Page<ActionEvent>> =
        ResponseEntity.ok(eventService.getActionEvents(page))

    @GetMapping(EVENTS_TEMPERATURE)
    fun getTemperatureEvents(@PathVariable page: Int): ResponseEntity<Page<TemperatureEvent>> =
        ResponseEntity.ok(eventService.getTemperatureEvents(page))
}

object EventEndpoints {
    const val EVENTS_ACTION = "/events/action/{page}"
    const val EVENTS_TEMPERATURE = "/events/temperature/{page}"
}