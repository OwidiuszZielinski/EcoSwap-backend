package org.example.ecoswapbackend.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: String? = null,
    val username: String,
    val firstName: String,
    val lastName: String,
    var points: Int = 0
) 