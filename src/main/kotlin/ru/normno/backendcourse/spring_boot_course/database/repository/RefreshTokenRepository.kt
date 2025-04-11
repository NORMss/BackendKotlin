package ru.normno.backendcourse.spring_boot_course.database.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import ru.normno.backendcourse.spring_boot_course.database.model.RefreshToken

@Component
interface RefreshTokenRepository : MongoRepository<RefreshToken, ObjectId> {
    fun findIdAndHashedToken(userId: ObjectId, hashedToken: String): RefreshToken?
    fun deleteByUserIdAndHashedToken(userId: ObjectId, hashedToken: String)
}