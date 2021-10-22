package com.mateuszjanczak.barrelsofbeer.security.data.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
data class User(
    @Id val id: String = ObjectId.get().toString(),
    @Indexed(unique = true) private val username: String,
    private val password: String,
    private val enabled: Boolean
) : UserDetails {

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = ArrayList()
}