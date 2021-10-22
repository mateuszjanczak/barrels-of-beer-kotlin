package com.mateuszjanczak.barrelsofbeer.security.service

import com.mateuszjanczak.barrelsofbeer.domain.data.document.User
import com.mateuszjanczak.barrelsofbeer.domain.service.UserService
import com.mateuszjanczak.barrelsofbeer.security.data.document.RefreshToken
import com.mateuszjanczak.barrelsofbeer.security.data.dto.Credentials
import com.mateuszjanczak.barrelsofbeer.security.data.dto.Token
import com.mateuszjanczak.barrelsofbeer.security.data.repository.RefreshTokenRepository
import com.mateuszjanczak.barrelsofbeer.security.token.TokenProvider
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface AuthService {
    fun login(credentials: Credentials): Token?
    fun refreshToken(refreshToken: String): Token?
    fun removeRefreshToken(refreshToken: String)
}

@Service
class DefaultAuthService(
    private val userService: UserService,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenRepository: RefreshTokenRepository
) : AuthService {

    override fun login(credentials: Credentials): Token? =
        userService.getUserByUsername(credentials.username)
            ?.takeIf { passwordEncoder.matches(credentials.password, it.password) }
            ?.let { createToken(it) }

    override fun refreshToken(refreshToken: String): Token? =
        refreshTokenRepository.findByRefreshToken(refreshToken)
            ?.let { userService.getUserById(it.userId) }
            ?.let { createToken(it) }

    override fun removeRefreshToken(refreshToken: String) {
        refreshTokenRepository.deleteAllByRefreshToken(refreshToken)
    }

    private fun createToken(user: User): Token {
        val token = tokenProvider.createToken(user.username)
        val expirationTime = tokenProvider.getExpirationTime()
        val refreshToken = createRefreshToken(user).refreshToken
        return Token(token, expirationTime, refreshToken)
    }

    private fun createRefreshToken(user: User): RefreshToken =
        refreshTokenRepository.deleteAllByUserId(user.id).let {
            refreshTokenRepository.save(
                RefreshToken(
                    refreshToken = randomAlphanumeric(128),
                    userId = user.id
                )
            )
        }
}

