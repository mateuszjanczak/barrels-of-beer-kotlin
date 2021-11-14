package com.mateuszjanczak.barrelsofbeer.security.data.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

interface ExtendedUserDetails : UserDetails {
    fun getId(): String
}

class DefaultUserDetails(
    private val id: String,
    private val username: String,
    private val password: String,
    private val enabled: Boolean
): ExtendedUserDetails {

    override fun getId(): String = id

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = ArrayList()

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled
}