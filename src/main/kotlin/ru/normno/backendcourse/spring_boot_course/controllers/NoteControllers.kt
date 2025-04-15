package ru.normno.backendcourse.spring_boot_course.controllers

import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import ru.normno.backendcourse.spring_boot_course.database.model.Note
import ru.normno.backendcourse.spring_boot_course.database.repository.NoteRepository
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteControllers(
    private val repository: NoteRepository,
) {
    data class NoteRequest(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long,
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant,

        )

    @PostMapping
    fun save(
        @RequestBody body: NoteRequest
    ): NoteResponse {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val note = repository.save(
            Note(
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId(ownerId),
            )
        )

        return NoteResponse(
            id = note.id.toHexString(),
            title = note.title,
            content = note.content,
            color = note.color,
            createdAt = note.createdAt,
        )
    }

    @GetMapping
    fun findByOwnerId(): List<NoteResponse> {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return repository.findByOwnerId(ObjectId(ownerId)).map {
            it.toResponse()
        }
    }

    private fun Note.toResponse(): NoteResponse {
        return NoteResponse(
            id = id.toHexString(),
            title = title,
            content = content,
            color = color,
            createdAt = createdAt
        )
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
        @PathVariable id: String,
    ) {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val note = repository.findById(ObjectId(id)).orElseThrow {
            IllegalArgumentException("Note not found")
        }
        if (note.ownerId.toHexString() == ownerId)
            repository.deleteById(ObjectId(id))
    }
}