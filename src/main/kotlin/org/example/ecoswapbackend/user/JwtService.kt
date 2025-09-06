package org.example.ecoswapbackend.user

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.example.ecoswapbackend.config.JwtConfig
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey
import kotlin.time.Duration.Companion.milliseconds

@Service
class JwtService(private val jwtConfig: JwtConfig) {
    
    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray())
    }
    
    fun generateToken(user: User): String {
        val now = Date()
        val expiration = Date(now.time + jwtConfig.expiration)
        
        return Jwts.builder()
            .subject(user.id)
            .claim("email", user.email)
            .claim("username", user.username)
            .claim("firstName", user.firstName)
            .claim("lastName", user.lastName)
            .claim("points", user.points)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(secretKey)
            .compact()
    }
    
    fun generateRefreshToken(user: User): String {
        val now = Date()
        val expiration = Date(now.time + jwtConfig.refresh.expiration)
        
        return Jwts.builder()
            .subject(user.id)
            .claim("type", "refresh")
            .issuedAt(now)
            .expiration(expiration)
            .signWith(secretKey)
            .compact()
    }
    
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun extractUserId(token: String): String? {
        return try {
            val claims = extractAllClaims(token)
            claims.subject
        } catch (e: Exception) {
            null
        }
    }

    
    fun isTokenExpired(token: String): Boolean {
        return try {
            val claims = extractAllClaims(token)
            claims.expiration.before(Date())
        } catch (e: Exception) {
            true
        }
    }
    
    fun isRefreshToken(token: String): Boolean {
        return try {
            val claims = extractAllClaims(token)
            claims["type"] == "refresh"
        } catch (e: Exception) {
            false
        }
    }
    
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }
    
    fun getTokenExpirationTime(token: String): Date? {
        return try {
            val claims = extractAllClaims(token)
            claims.expiration
        } catch (e: Exception) {
            null
        }
    }
    
    fun getTokenRemainingTime(token: String): Long {
        val expiration = getTokenExpirationTime(token) ?: return 0
        val now = Date()
        return maxOf(0, expiration.time - now.time)
    }
}