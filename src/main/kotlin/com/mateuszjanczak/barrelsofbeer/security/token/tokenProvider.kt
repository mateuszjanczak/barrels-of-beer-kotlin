package com.mateuszjanczak.barrelsofbeer.security.token

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS512
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant.now
import java.util.Date

interface TokenProvider {
    fun createToken(subject: String): String
    fun getUsernameFromToken(token: String): String
    fun getExpirationTime(): Long
}

@Component
class DefaultTokenProvider(
    @Value("\${token.secret}") private val secret: String,
    @Value("\${token.expirationTime}") private val expirationTime: Long
) : TokenProvider {

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
    }

    override fun createToken(subject: String): String =
        Jwts.builder()
            .setSubject(subject)
            .setExpiration(Date.from(now().plusSeconds(expirationTime)))
            .signWith(HS512, secret.toByteArray())
            .compact()

    override fun getUsernameFromToken(token: String): String =
        Jwts.parser()
            .setSigningKey(secret.toByteArray())
            .parseClaimsJws(token)
            .body
            .subject

    override fun getExpirationTime(): Long = expirationTime
}