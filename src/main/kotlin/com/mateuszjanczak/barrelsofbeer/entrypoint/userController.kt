package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.domain.data.document.User
import com.mateuszjanczak.barrelsofbeer.domain.service.UserService
import com.mateuszjanczak.barrelsofbeer.entrypoint.UserEndpoints.DISABLE_USER
import com.mateuszjanczak.barrelsofbeer.entrypoint.UserEndpoints.ENABLE_USER
import com.mateuszjanczak.barrelsofbeer.entrypoint.UserEndpoints.USERS
import com.mateuszjanczak.barrelsofbeer.entrypoint.UserEndpoints.USER_ID
import com.mateuszjanczak.barrelsofbeer.security.data.Credentials

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@CrossOrigin
@RequestMapping(value = ["/api"])
class UserController(
    private val userService: UserService
) {

    @GetMapping(USER_ID)
    fun getUserById(@PathVariable userId: String): ResponseEntity<User> = ok(userService.getUserById(userId))

    @GetMapping(USERS)
    fun getUsers(): ResponseEntity<List<User>> = ok(userService.getUsers())

    @PostMapping(USERS)
    fun createUser(@Valid @RequestBody credentials: Credentials): ResponseEntity<Unit> = ok(userService.createUser(credentials))

    @PostMapping(ENABLE_USER)
    fun enableUser(@PathVariable userId: String): ResponseEntity<Unit> = ok(userService.toggleUserStatus(userId, true))

    @PostMapping(DISABLE_USER)
    fun disableUser(@PathVariable userId: String): ResponseEntity<Unit> = ok(userService.toggleUserStatus(userId, false))

}

object UserEndpoints {
    const val USERS = "/users"
    const val USER_ID = "/users/{userId}"
    const val ENABLE_USER = "/users/{userId}/enable"
    const val DISABLE_USER = "/users/{userId}/disable"
}