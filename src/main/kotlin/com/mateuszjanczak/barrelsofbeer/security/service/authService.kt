package com.mateuszjanczak.barrelsofbeer.security.service

import com.mateuszjanczak.barrelsofbeer.security.common.AccountNotEnabledException
import com.mateuszjanczak.barrelsofbeer.security.common.InvalidPasswordException
import com.mateuszjanczak.barrelsofbeer.security.common.TokenNotFoundException
import com.mateuszjanczak.barrelsofbeer.security.common.UserNotFoundException
import com.mateuszjanczak.barrelsofbeer.security.data.document.RefreshToken
import com.mateuszjanczak.barrelsofbeer.security.data.dto.Credentials
import com.mateuszjanczak.barrelsofbeer.security.data.dto.Token
import com.mateuszjanczak.barrelsofbeer.security.data.model.ExtendedUserDetails
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
    private val extendedUserDetailsService: ExtendedUserDetailsService,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenRepository: RefreshTokenRepository
) : AuthService {

    override fun login(credentials: Credentials): Token? {
        val user = extendedUserDetailsService.loadUserByUsername(credentials.username) ?: throw UserNotFoundException()
        if(!passwordEncoder.matches(credentials.password, user.password)) throw InvalidPasswordException()
        if(!user.isEnabled) throw AccountNotEnabledException()
        return createToken(user)
    }

    override fun refreshToken(refreshToken: String): Token? {
        val token = refreshTokenRepository.findByRefreshToken(refreshToken) ?: throw TokenNotFoundException()
        val user = extendedUserDetailsService.getUserById(token.userId) ?: throw UserNotFoundException()
        return createToken(user)
    }

    override fun removeRefreshToken(refreshToken: String) {
        refreshTokenRepository.deleteAllByRefreshToken(refreshToken)
    }

    private fun createToken(user: ExtendedUserDetails): Token {
        val token = tokenProvider.createToken(user.username)
        val expirationTime = tokenProvider.getExpirationTime()
        val refreshToken = createRefreshToken(user).refreshToken
        return Token(token, expirationTime, refreshToken)
    }

    private fun createRefreshToken(user: ExtendedUserDetails): RefreshToken =
        refreshTokenRepository.deleteAllByUserId(user.getId()).let {
            refreshTokenRepository.save(
                RefreshToken(
                    refreshToken = randomAlphanumeric(128),
                    userId = user.getId()
                )
            )
        }
}

