package ru.normno.backendcourse.spring_boot_course.controllers

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.normno.backendcourse.spring_boot_course.database.model.User
import ru.normno.backendcourse.spring_boot_course.security.AuthService

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    data class AuthRequest(
        @field:Email(message = "Invalid email format")
        val email: String,
        @field:Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}\$",
            message = "Password must be at least 9 characters long and contain at least one digit, uppercase and lowercase character.",
        )
        val password: String,
    )

    data class RefreshRequest(
        val refreshToken: String,
    )

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody authRequest: AuthRequest,
    ): User {
        return authService.register(
            email = authRequest.email,
            password = authRequest.password,
        )
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody authRequest: AuthRequest,
    ): AuthService.TokenPair {
        return authService.login(
            email = authRequest.email,
            password = authRequest.password,
        )
    }

    @PostMapping("/refresh")
    fun refresh(
        @Valid @RequestBody refreshRequest: RefreshRequest,
    ) {
        authService.refresh(
            refreshToken = refreshRequest.refreshToken,
        )
    }
}