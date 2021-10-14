package com.mateuszjanczak.barrelsofbeer.domain.data.repository

import com.mateuszjanczak.barrelsofbeer.domain.data.document.TapTemperatureLog
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TapTemperatureLogRepository : MongoRepository<TapTemperatureLog, String>