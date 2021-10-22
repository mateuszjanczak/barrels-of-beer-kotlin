package com.mateuszjanczak.barrelsofbeer.security.entrypoint

import com.mateuszjanczak.barrelsofbeer.security.data.Credentials
import com.mateuszjanczak.barrelsofbeer.security.data.Token
import com.mateuszjanczak.barrelsofbeer.security.entrypoint.AuthEndpoints.LOGIN
import com.mateuszjanczak.barrelsofbeer.security.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.CrossOrigin
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

}

object AuthEndpoints {
    const val LOGIN = "/api/login"
}