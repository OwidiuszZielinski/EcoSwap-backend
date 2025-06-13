package org.example.ecoswapbackend.message

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "messages")
data class Message(
    @Id
    val id: String? = null,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Instant = Instant.now(),
    val read: Boolean = false
)

data class MessageDto(
    val id: String?,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Instant,
    val read: Boolean
)

fun Message.toDto() = MessageDto(
    id = id,
    senderId = senderId,
    receiverId = receiverId,
    content = content,
    timestamp = timestamp,
    read = read
) 