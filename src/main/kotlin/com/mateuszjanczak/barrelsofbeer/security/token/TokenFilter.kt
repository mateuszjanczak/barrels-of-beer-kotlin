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
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TokenFilter(
    private val userService: UserService,
    private val tokenProvider: TokenProvider
) : GenericFilterBean() {

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse

        try {
            val header = request.getHeader(AUTHORIZATION_HEADER)
            val token = header.getToken()
            val authentication = getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: JwtException) {
            response.status = UNAUTHORIZED.value()
            response.contentType = APPLICATION_JSON_VALUE;
            response.outputStream.write(ErrorMessage(e.message ?: "Invalid token", UNAUTHORIZED.name).toJson().toByteArray())
            return
        } catch (e: Exception) {
            response.status = UNAUTHORIZED.value()
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun String.getToken() = this.replace(TOKEN_PREFIX, "")

    private fun getAuthentication(token: String): Authentication? {
        val username = tokenProvider.getUsernameFromToken(token)
        return userService.getUserByUsername(username)?.let { user ->
            UsernamePasswordAuthenticationToken(user, user.password, user.authorities)
        }
    }
}