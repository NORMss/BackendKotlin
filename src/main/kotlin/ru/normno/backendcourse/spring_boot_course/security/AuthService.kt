package ru.normno.backendcourse.spring_boot_course.security

import org.springframework.stereotype.Service
import ru.normno.backendcourse.spring_boot_course.database.repository.UserRepository

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder
) {
}