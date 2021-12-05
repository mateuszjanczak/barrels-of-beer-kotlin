package com.mateuszjanczak.barrelsofbeer.domain.data.repository

import com.mateuszjanczak.barrelsofbeer.domain.data.document.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun findUserById(id: String): User?
}