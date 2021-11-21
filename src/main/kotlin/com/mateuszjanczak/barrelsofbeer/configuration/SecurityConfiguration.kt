package com.mateuszjanczak.barrelsofbeer.configuration

import com.mateuszjanczak.barrelsofbeer.entrypoint.UserEndpoints.USERS
import com.mateuszjanczak.barrelsofbeer.security.entrypoint.AuthEndpoints.LOGIN
import com.mateuszjanczak.barrelsofbeer.security.entrypoint.AuthEndpoints.REFRESH_TOKEN
import com.mateuszjanczak.barrelsofbeer.security.entrypoint.AuthEndpoints.REMOVE_REFRESH_TOKEN
import com.mateuszjanczak.barrelsofbeer.security.token.TokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val tokenFilter: TokenFilter
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.cors()
        http.csrf().disable()
        http.addFilterAfter(tokenFilter, BasicAuthenticationFilter::class.java)
        http.authorizeRequests()
            .antMatchers("/*", "/static/**").permitAll()
            .antMatchers(LOGIN, REFRESH_TOKEN, REMOVE_REFRESH_TOKEN).permitAll()
            .antMatchers(POST, USERS).permitAll()
            .anyRequest().authenticated()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}