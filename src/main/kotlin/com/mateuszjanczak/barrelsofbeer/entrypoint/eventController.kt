package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.domain.data.document.ActionEvent
import com.mateuszjanczak.barrelsofbeer.domain.data.document.TemperatureEvent
import com.mateuszjanczak.barrelsofbeer.domain.service.DocumentService
import com.mateuszjanczak.barrelsofbeer.domain.service.EventService
import com.mateuszjanczak.barrelsofbeer.entrypoint.EventEndpoints.EVENTS_ACTION
import com.mateuszjanczak.barrelsofbeer.entrypoint.EventEndpoints.EVENTS_ACTION_CSV
import com.mateuszjanczak.barrelsofbeer.entrypoint.EventEndpoints.EVENTS_TEMPERATURE
import com.mateuszjanczak.barrelsofbeer.entrypoint.EventEndpoints.EVENTS_TEMPERATURE_CSV
import org.springframework.core.io.Resource
import org.springframework.data.domain.Page
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ofPattern

@RestController
@CrossOrigin
@RequestMapping(value = ["/api"])
class EventController(
    private val eventService: EventService,
    private val documentService: DocumentService
) {

    @GetMapping(EVENTS_ACTION)
    fun getActionEvents(@PathVariable page: Int): ResponseEntity<Page<ActionEvent>> =
        ok(eventService.getActionEvents(page))

    @GetMapping(EVENTS_TEMPERATURE)
    fun getTemperatureEvents(@PathVariable page: Int): ResponseEntity<Page<TemperatureEvent>> =
        ok(eventService.getTemperatureEvents(page))

    @GetMapping(EVENTS_ACTION_CSV)
    fun downloadActionEventsAsCsv(): ResponseEntity<Resource> =
        ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=actionEvents-${date()}.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(documentService.getActionEventsCsv())

    @GetMapping(EVENTS_TEMPERATURE_CSV)
    fun downloadTemperatureEventsAsCsv(): ResponseEntity<Resource> =
        ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=temperatureEvents-${date()}.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(documentService.getTemperatureEventsCsv())

    private fun date() = now().format(ofPattern("yyyy-MM-dd HH-mm-ss"))
}

object EventEndpoints {
    const val EVENTS_ACTION = "/events/action/{page}"
    const val EVENTS_TEMPERATURE = "/events/temperature/{page}"
    const val EVENTS_ACTION_CSV = "/events/action/download"
    const val EVENTS_TEMPERATURE_CSV = "/events/temperature/download"
}