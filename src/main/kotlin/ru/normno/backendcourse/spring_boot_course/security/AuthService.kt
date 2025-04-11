package ru.normno.backendcourse.spring_boot_course.security

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import ru.normno.backendcourse.spring_boot_course.database.model.User
import ru.normno.backendcourse.spring_boot_course.database.repository.UserRepository

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder
) {
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String,
    )

    fun register(email: String, password: String): User {
        return userRepository.save(
            User(
                email = email,
                hashedPassword = hashEncoder.encode(password),
            )
        )
    }

    fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email)
            ?: throw BadCredentialsException("Invalid credentials.")
        if (!hashEncoder.matches(password, user.hashedPassword)) {
            throw BadCredentialsException("Invalid credentials.")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
        )
    }
}