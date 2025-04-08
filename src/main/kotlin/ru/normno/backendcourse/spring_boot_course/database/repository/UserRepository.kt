package ru.normno.backendcourse.spring_boot_course.database.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import ru.normno.backendcourse.spring_boot_course.database.model.User

interface UserRepository : MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
}