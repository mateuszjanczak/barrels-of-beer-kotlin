package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.domain.data.dto.Ranking
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.Statistics
import com.mateuszjanczak.barrelsofbeer.domain.service.StatisticsService
import com.mateuszjanczak.barrelsofbeer.entrypoint.StatisticsEndpoints.RANKING
import com.mateuszjanczak.barrelsofbeer.entrypoint.StatisticsEndpoints.STATISTICS
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime


@RestController
@CrossOrigin
@RequestMapping
class StatisticsController(
    private val statisticsService: StatisticsService
) {

    @GetMapping(RANKING)
    fun getRanking(): ResponseEntity<List<Ranking>> =
        ok(statisticsService.getRanking())

    @GetMapping(STATISTICS)
    fun getStatistics(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") from: LocalDateTime,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") to: LocalDateTime,
        @PathVariable interval: Long
    ): ResponseEntity<List<Statistics>> =
        ok(statisticsService.getStatistics(from, to, interval))

}

object StatisticsEndpoints {
    const val RANKING = "/api/ranking"
    const val STATISTICS = "/api/statistics/from/{from}/to/{to}/interval/{interval}"
}