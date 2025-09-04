package org.example.ecoswapbackend.user

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.example.ecoswapbackend.user.UserService
import org.example.ecoswapbackend.user.JwtService

@RestController
@RequestMapping("/api")
class AuthController(
        val userService: UserService,
        val jwtService: JwtService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val user = userService.authenticate(loginRequest.email, loginRequest.password)
        return if (user != null) {
            val token = jwtService.generateToken(user)
            ResponseEntity.ok(LoginResponse(token))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)