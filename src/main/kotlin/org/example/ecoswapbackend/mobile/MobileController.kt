package org.example.ecoswapbackend.mobile

import org.example.ecoswapbackend.user.User
import org.example.ecoswapbackend.user.UserService
import org.example.ecoswapbackend.user.JwtService
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/mobile")
class MobileController(
    private val userService: UserService,
    private val jwtService: JwtService
) {

    @GetMapping("/profile")
    fun getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String): ResponseEntity<MobileProfileResponse> {
        val token = authHeader.removePrefix("Bearer ")
        
        if (!jwtService.validateToken(token) || jwtService.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(MobileProfileResponse(error = "Invalid or expired token"))
        }
        
        val userId = jwtService.extractUserId(token)
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(MobileProfileResponse(error = "Invalid token"))
        }
        
        val user = userService.getUserById(userId)
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(MobileProfileResponse(error = "User not found"))
        }
        
        val remainingTime = jwtService.getTokenRemainingTime(token)
        
        return ResponseEntity.ok(MobileProfileResponse(
            user = MobileUserInfo(
                id = user.id,
                email = user.email,
                username = user.username,
                firstName = user.firstName,
                lastName = user.lastName,
                points = user.points
            ),
            tokenInfo = TokenInfo(
                expiresIn = remainingTime,
                tokenType = "Bearer"
            )
        ))
    }

    @PutMapping("/profile")
    fun updateProfile(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestBody updateRequest: MobileProfileUpdateRequest
    ): ResponseEntity<MobileProfileResponse> {
        val token = authHeader.removePrefix("Bearer ")
        
        if (!jwtService.validateToken(token) || jwtService.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(MobileProfileResponse(error = "Invalid or expired token"))
        }
        
        val userId = jwtService.extractUserId(token)
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(MobileProfileResponse(error = "Invalid token"))
        }
        
        val user = userService.getUserById(userId)
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(MobileProfileResponse(error = "User not found"))
        }
        
        val updatedUser = user.copy(
            firstName = updateRequest.firstName ?: user.firstName,
            lastName = updateRequest.lastName ?: user.lastName
        )
        
        val savedUser = userService.updateUser(userId, updatedUser)
        if (savedUser == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(MobileProfileResponse(error = "Failed to update profile"))
        }
        
        val remainingTime = jwtService.getTokenRemainingTime(token)
        
        return ResponseEntity.ok(MobileProfileResponse(
            user = MobileUserInfo(
                id = savedUser.id,
                email = savedUser.email,
                username = savedUser.username,
                firstName = savedUser.firstName,
                lastName = savedUser.lastName,
                points = savedUser.points
            ),
            tokenInfo = TokenInfo(
                expiresIn = remainingTime,
                tokenType = "Bearer"
            )
        ))
    }

    @GetMapping("/token-info")
    fun getTokenInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String): ResponseEntity<TokenInfoResponse> {
        val token = authHeader.removePrefix("Bearer ")
        
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(TokenInfoResponse(
                    valid = false,
                    expired = true,
                    expiresIn = 0,
                    isRefreshToken = false,
                    tokenType = "Bearer",
                    error = "Invalid token"
                ))
        }
        
        val isExpired = jwtService.isTokenExpired(token)
        val remainingTime = if (isExpired) 0 else jwtService.getTokenRemainingTime(token)
        val isRefreshToken = jwtService.isRefreshToken(token)
        
        return ResponseEntity.ok(TokenInfoResponse(
            valid = !isExpired,
            expired = isExpired,
            expiresIn = remainingTime,
            isRefreshToken = isRefreshToken,
            tokenType = "Bearer"
        ))
    }
}

// Request/Response classes
data class MobileProfileUpdateRequest(
    val firstName: String? = null,
    val lastName: String? = null
)

data class MobileProfileResponse(
    val user: MobileUserInfo? = null,
    val tokenInfo: TokenInfo? = null,
    val error: String? = null
)

data class TokenInfoResponse(
    val valid: Boolean,
    val expired: Boolean,
    val expiresIn: Long,
    val isRefreshToken: Boolean,
    val tokenType: String,
    val error: String? = null
)

data class TokenInfo(
    val expiresIn: Long,
    val tokenType: String
)

data class MobileUserInfo(
    val id: String?,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val points: Int
)
