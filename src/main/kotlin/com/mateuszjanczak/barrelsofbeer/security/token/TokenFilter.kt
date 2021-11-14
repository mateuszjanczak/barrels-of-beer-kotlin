package com.mateuszjanczak.barrelsofbeer.security.token

import com.mateuszjanczak.barrelsofbeer.domain.data.dto.ErrorMessage
import com.mateuszjanczak.barrelsofbeer.domain.service.UserService
import com.mateuszjanczak.barrelsofbeer.security.token.DefaultTokenProvider.Companion.AUTHORIZATION_HEADER
import com.mateuszjanczak.barrelsofbeer.security.token.DefaultTokenProvider.Companion.TOKEN_PREFIX
import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TokenFilter(
    private val userService: UserService,
    private val tokenProvider: TokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val header = request.getHeader(AUTHORIZATION_HEADER)
            if(header != null && header.startsWith(TOKEN_PREFIX)) {
                val token = header.replace(TOKEN_PREFIX, "")
                SecurityContextHolder.getContext().authentication = getAuthentication(token)
            }
        } catch (e: JwtException) {
            response.status = UNAUTHORIZED.value()
            response.contentType = APPLICATION_JSON_VALUE
            response.outputStream.write(ErrorMessage(e.message ?: "Invalid token", UNAUTHORIZED.name).toJson().toByteArray())
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun getAuthentication(token: String): Authentication? {
        val username = tokenProvider.getUsernameFromToken(token)
        return userService.getUserByUsername(username)?.let { user ->
            UsernamePasswordAuthenticationToken(user, user.password, user.authorities)
        }
    }
}