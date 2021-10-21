package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.domain.data.dto.Ranking
import com.mateuszjanczak.barrelsofbeer.domain.service.StatisticsService
import com.mateuszjanczak.barrelsofbeer.entrypoint.StatisticsEndpoints.RANKING
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping(value = ["/api"])
class StatisticsController(
    private val statisticsService: StatisticsService
) {

    @GetMapping(RANKING)
    fun getRanking(): ResponseEntity<List<Ranking>> =
        ok(statisticsService.getRanking())
}

object StatisticsEndpoints {
    const val RANKING = "/ranking"
}