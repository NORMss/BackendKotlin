package ru.normno.backendcourse.spring_boot_course.database.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import ru.normno.backendcourse.spring_boot_course.database.model.Note

interface NoteRepository : MongoRepository<Note, ObjectId> {
    fun findByOwnerId(ownerId: ObjectId): List<Note>
}