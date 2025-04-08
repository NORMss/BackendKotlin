package ru.normno.backendcourse.spring_boot_course.controllers

import org.bson.types.ObjectId
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
        val ownerId: String,
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant,

        )

    @PostMapping
    fun save(body: NoteRequest): NoteResponse {
        val note = repository.save(
            Note(
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId(body.ownerId),
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
    fun findByOwnerId(
        @RequestParam(required = true) ownerId: String,
    ): List<NoteResponse> {
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
}