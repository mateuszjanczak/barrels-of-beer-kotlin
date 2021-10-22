package com.mateuszjanczak.barrelsofbeer.security.service

import com.mateuszjanczak.barrelsofbeer.security.data.document.User
import com.mateuszjanczak.barrelsofbeer.security.data.dto.NewUser
import com.mateuszjanczak.barrelsofbeer.security.data.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

interface UserService : UserDetailsService {
    fun createUser(newUser: NewUser)
    fun getUsers(): List<User>
    fun getUser(userId: String): User?
    fun removeUser(userId: String)
    fun toggleUserStatus(userId: String, enabled: Boolean)
}

@Service
class DefaultUserService(
    private val userRepository: UserRepository
) : UserService {

    override fun createUser(newUser: NewUser) {
        userRepository.save(
            User(
                username = newUser.username,
                password = newUser.password,
                enabled = false
            )
        )
    }

    override fun getUsers(): List<User> = userRepository.findAll()

    override fun getUser(userId: String): User? = userRepository.findByIdOrNull(userId)

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