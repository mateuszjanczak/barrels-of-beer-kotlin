package com.mateuszjanczak.barrelsofbeer.domain.data.repository

import com.mateuszjanczak.barrelsofbeer.domain.data.document.ActionEvent
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ActionEventRepository : MongoRepository<ActionEvent, String> {
    fun findActionEventByTapIdOrderByIdDesc(tapId: Int): List<ActionEvent>
}