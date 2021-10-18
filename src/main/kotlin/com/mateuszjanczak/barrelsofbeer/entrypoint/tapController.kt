package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.domain.TapService
import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.NewTap
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.TapDetails
import com.mateuszjanczak.barrelsofbeer.entrypoint.TapEndpoints.TAPS
import com.mateuszjanczak.barrelsofbeer.entrypoint.TapEndpoints.TAP_ID
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@CrossOrigin
@RequestMapping(value = ["/api"])
class TapController(
    private val tapService: TapService
) {

    @GetMapping(TAP_ID)
    fun getTapById(@PathVariable tapId: Int): ResponseEntity<Tap> = ok(tapService.getTap(tapId))

    @GetMapping(TAPS)
    fun getTaps(): ResponseEntity<List<Tap>> = ok(tapService.getTapList())

    @PostMapping(TAPS)
    fun createTap(@Valid @RequestBody newTap: NewTap): ResponseEntity<Unit> = ok(tapService.createTap(newTap.tapId))

    @PostMapping(TAP_ID)
    fun setTap(@PathVariable tapId: Int, @Valid @RequestBody tapDetails: TapDetails): ResponseEntity<Unit> = ok(tapService.setTap(tapId, tapDetails))
}

object TapEndpoints {
    const val TAPS = "/taps"
    const val TAP_ID = "/taps/{tapId}"
}