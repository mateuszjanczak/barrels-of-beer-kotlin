package com.mateuszjanczak.barrelsofbeer.domain.data.repository

import com.mateuszjanczak.barrelsofbeer.domain.data.document.TemperatureEvent
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TemperatureEventRepository : MongoRepository<TemperatureEvent, String>