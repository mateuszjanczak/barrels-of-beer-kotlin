package com.mateuszjanczak.barrelsofbeer.security.data.repository

import com.mateuszjanczak.barrelsofbeer.security.data.document.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String>