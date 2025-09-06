package org.example.ecoswapbackend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
data class JwtConfig(
    var secret: String = "",
    var expiration: Long = 0,
    var refresh: RefreshTokenConfig = RefreshTokenConfig()
)

data class RefreshTokenConfig(
    var expiration: Long = 0
)
