package com.mateuszjanczak.barrelsofbeer.security.data.repository

import com.mateuszjanczak.barrelsofbeer.security.data.document.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {
    fun findByRefreshToken(refreshToken: String): RefreshToken?
    fun deleteAllByUserId(userId: String)
    fun deleteAllByRefreshToken(refreshToken: String)
}