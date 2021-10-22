package com.mateuszjanczak.barrelsofbeer.security.entrypoint

import com.mateuszjanczak.barrelsofbeer.security.data.dto.Credentials
import com.mateuszjanczak.barrelsofbeer.security.data.dto.Token
import com.mateuszjanczak.barrelsofbeer.security.entrypoint.AuthEndpoints.LOGIN
import com.mateuszjanczak.barrelsofbeer.security.entrypoint.AuthEndpoints.REFRESH_TOKEN
import com.mateuszjanczak.barrelsofbeer.security.entrypoint.AuthEndpoints.REMOVE_REFRESH_TOKEN
import com.mateuszjanczak.barrelsofbeer.security.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@CrossOrigin
@RequestMapping
class AuthController(
    private val authService: AuthService
) {

    @PostMapping(LOGIN)
    fun login(@Valid @RequestBody credentials: Credentials): ResponseEntity<Token> = ok(authService.login(credentials))

    @PostMapping(REFRESH_TOKEN)
    fun refreshToken(@PathVariable refreshToken: String): ResponseEntity<Token> = ok(authService.refreshToken(refreshToken))

    @PostMapping(REMOVE_REFRESH_TOKEN)
    fun removeRefreshToken(@PathVariable refreshToken: String): ResponseEntity<Unit> = ok(authService.removeRefreshToken(refreshToken))

}

object AuthEndpoints {
    const val LOGIN = "/api/auth/login"
    const val REFRESH_TOKEN = "/api/auth/refreshToken/{refreshToken}"
    const val REMOVE_REFRESH_TOKEN = "/api/auth/remove/{refreshToken}"
}