package com.mateuszjanczak.barrelsofbeer.domain.service

import com.mateuszjanczak.barrelsofbeer.domain.data.document.User
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.UserRepository
import com.mateuszjanczak.barrelsofbeer.security.data.dto.Credentials
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService {
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
    private val passwordEncoder: PasswordEncoder
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
}