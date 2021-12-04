package com.mateuszjanczak.barrelsofbeer.domain.data.repository

import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TapRepository : MongoRepository<Tap, Int> {
    fun findByTapId(tapId: Int): Tap?
}