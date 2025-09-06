package org.example.ecoswapbackend.user

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.http.HttpHeaders
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

            val refreshToken = jwtService.generateRefreshToken(user)
            ResponseEntity.ok(LoginResponse(token, refreshToken, user))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PostMapping("/mobile/login")
    fun mobileLogin(@RequestBody loginRequest: MobileLoginRequest): ResponseEntity<MobileLoginResponse> {
        val user = userService.authenticate(loginRequest.email, loginRequest.password)
        return if (user != null) {
            val token = jwtService.generateToken(user)
            val refreshToken = jwtService.generateRefreshToken(user)
            val remainingTime = jwtService.getTokenRemainingTime(token)
            println("Wygenerowano token i zautoryzowano aplikacje tokenem : "+  token)
            ResponseEntity.ok(MobileLoginResponse(
                accessToken = token,
                refreshToken = refreshToken,
                tokenType = "Bearer",
                expiresIn = remainingTime,
                user = MobileUserInfo(
                    id = user.id,
                    email = user.email,
                    username = user.username,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    points = user.points
                )
            ))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(MobileLoginResponse(error = "Invalid credentials"))
        }
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody refreshRequest: RefreshTokenRequest): ResponseEntity<RefreshTokenResponse> {
        val refreshToken = refreshRequest.refreshToken
        
        if (!jwtService.validateToken(refreshToken) || !jwtService.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(RefreshTokenResponse(error = "Invalid refresh token"))
        }
        
        val userId = jwtService.extractUserId(refreshToken)
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(RefreshTokenResponse(error = "Invalid refresh token"))
        }
        
        val user = userService.getUserById(userId)
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(RefreshTokenResponse(error = "User not found"))
        }
        
        val newToken = jwtService.generateToken(user)
        val newRefreshToken = jwtService.generateRefreshToken(user)
        val remainingTime = jwtService.getTokenRemainingTime(newToken)
        
        return ResponseEntity.ok(RefreshTokenResponse(
            accessToken = newToken,
            refreshToken = newRefreshToken,
            tokenType = "Bearer",
            expiresIn = remainingTime
        ))
    }

    @GetMapping("/mobile/validate")
    fun validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String): ResponseEntity<TokenValidationResponse> {
        val token = authHeader.removePrefix("Bearer ")
        
        if (!jwtService.validateToken(token) || jwtService.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(TokenValidationResponse(valid = false, error = "Invalid or expired token"))
        }
        
        val userId = jwtService.extractUserId(token)
        val user = userId?.let { userService.getUserById(it) }
        
        return if (user != null) {
            val remainingTime = jwtService.getTokenRemainingTime(token)
            ResponseEntity.ok(TokenValidationResponse(
                valid = true,
                user = MobileUserInfo(
                    id = user.id,
                    email = user.email,
                    username = user.username,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    points = user.points
                ),
                expiresIn = remainingTime
            ))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(TokenValidationResponse(valid = false, error = "User not found"))
        }
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String): ResponseEntity<LogoutResponse> {
        return ResponseEntity.ok(LogoutResponse(message = "Successfully logged out"))
    }
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val refreshToken: String, val user: User)

data class MobileLoginRequest(val email: String, val password: String)
data class MobileLoginResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val tokenType: String? = null,
    val expiresIn: Long? = null,
    val user: MobileUserInfo? = null,
    val error: String? = null
)

data class RefreshTokenRequest(val refreshToken: String)
data class RefreshTokenResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val tokenType: String? = null,
    val expiresIn: Long? = null,
    val error: String? = null
)

data class TokenValidationResponse(
    val valid: Boolean,
    val user: MobileUserInfo? = null,
    val expiresIn: Long? = null,
    val error: String? = null
)

data class LogoutResponse(val message: String)

data class MobileUserInfo(
    val id: String?,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val points: Int
)