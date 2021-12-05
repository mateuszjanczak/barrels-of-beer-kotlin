package com.mateuszjanczak.barrelsofbeer.security.service

import com.mateuszjanczak.barrelsofbeer.domain.data.repository.UserRepository
import com.mateuszjanczak.barrelsofbeer.security.data.model.DefaultUserDetails
import com.mateuszjanczak.barrelsofbeer.security.data.model.ExtendedUserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

interface ExtendedUserDetailsService : UserDetailsService {
    override fun loadUserByUsername(username: String): ExtendedUserDetails?
    fun getUserById(userId: String): ExtendedUserDetails?
}

@Service
class DefaultUserDetailsService(
    private val userRepository: UserRepository
) : ExtendedUserDetailsService {

    override fun loadUserByUsername(username: String): ExtendedUserDetails? =
        userRepository.findByUsername(username)?.let {
            DefaultUserDetails(
                id = it.id,
                username = it.username,
                password = it.password,
                enabled = it.enabled
            )
        }

    override fun getUserById(userId: String): ExtendedUserDetails? =
        userRepository.findUserById(userId)?.let {
            DefaultUserDetails(
                id = it.id,
                username = it.username,
                password = it.password,
                enabled = it.enabled
            )
        }
}

