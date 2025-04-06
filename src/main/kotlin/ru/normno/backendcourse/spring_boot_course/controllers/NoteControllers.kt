package ru.normno.backendcourse.spring_boot_course.controllers

class NoteControllers {
    data class NoteRequest(
        val title: String,
        val content: String,
        val color: Long,
    )

    fun save(){

    }
}