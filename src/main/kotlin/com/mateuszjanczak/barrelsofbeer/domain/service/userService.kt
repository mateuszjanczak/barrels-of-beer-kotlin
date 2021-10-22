package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.domain.data.document.User
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.UserRepository
import com.mateuszjanczak.barrelsofbeer.security.data.Credentials
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService : UserDetailsService {
    fun createUser(credentials: Credentials)
    fun getUsers(): List<User>
    fun getUserById(id: String): User?
    fun getUserByUsername(username: String): User?
    fun removeUser(userId: String)
    fun toggleUserStatus(userId: String, enabled: Boolean)
}

@Service
class DefaultUserService(
    private val userRepository: UserRepository,
    @Lazy private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun createUser(credentials: Credentials) {
        userRepository.save(
            User(
                username = credentials.username,
                password = passwordEncoder.encode(credentials.password),
                enabled = false
            )
        )
    }

    override fun getUsers(): List<User> = userRepository.findAll()

    override fun getUserById(id: String): User? = userRepository.findByIdOrNull(id)

    override fun getUserByUsername(username: String): User? = userRepository.findByUsername(username)

    override fun removeUser(userId: String) = userRepository.deleteById(userId)

    override fun toggleUserStatus(userId: String, enabled: Boolean) {
        userRepository.findByIdOrNull(userId)?.let { previous ->
            userRepository.save(
                User(
                    id = previous.id,
                    username = previous.username,
                    password = previous.password,
                    enabled = enabled
                )
            )
        }
    }

    override fun loadUserByUsername(username: String): User =
        userRepository.findById(username).orElseThrow { UsernameNotFoundException(username) }

}