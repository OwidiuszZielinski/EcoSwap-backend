package org.example.ecoswapbackend.user

import org.springframework.stereotype.Service

@Service
class JwtService {
    fun generateToken(user: User): String {
        // Tu powinna być prawdziwa logika generowania JWT
        // Na razie zwracamy przykładowy token
        return "mocked-jwt-token-for-${user.email}"
    }
}