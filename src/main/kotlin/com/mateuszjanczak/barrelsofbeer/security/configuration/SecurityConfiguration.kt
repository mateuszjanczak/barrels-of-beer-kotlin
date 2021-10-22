package com.mateuszjanczak.barrelsofbeer.security.configuration

import com.mateuszjanczak.barrelsofbeer.security.entrypoint.AuthEndpoints.LOGIN
import com.mateuszjanczak.barrelsofbeer.security.token.TokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val tokenFilter: TokenFilter
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.cors().disable()
        http.csrf().disable()
        http.addFilterAfter(tokenFilter, BasicAuthenticationFilter::class.java)
        http.authorizeRequests()
            .antMatchers(LOGIN).permitAll()
            .anyRequest().authenticated()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}