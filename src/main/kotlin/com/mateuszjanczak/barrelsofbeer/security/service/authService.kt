package com.mateuszjanczak.barrelsofbeer.security.service

import com.mateuszjanczak.barrelsofbeer.domain.data.document.User
import com.mateuszjanczak.barrelsofbeer.domain.service.UserService
import com.mateuszjanczak.barrelsofbeer.security.data.Credentials
import com.mateuszjanczak.barrelsofbeer.security.data.Token
import com.mateuszjanczak.barrelsofbeer.security.token.TokenProvider
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface AuthService {
    fun login(credentials: Credentials): Token?
}

@Service
class DefaultAuthService(
    private val userService: UserService,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: PasswordEncoder
) : AuthService {

    override fun login(credentials: Credentials): Token? =
        userService.getUserByUsername(credentials.username)
            ?.takeIf { passwordEncoder.matches(credentials.password, it.password) }
            ?.let { createToken(it) }

    private fun createToken(user: User): Token {
        val token = tokenProvider.createToken(user.username)
        val expirationTime = tokenProvider.getExpirationTime()
        val refreshToken = randomAlphanumeric(128)
        return Token(token, expirationTime, refreshToken)
    }
}

