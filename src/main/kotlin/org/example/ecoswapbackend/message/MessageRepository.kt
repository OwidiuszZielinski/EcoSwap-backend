package org.example.ecoswapbackend.message

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : MongoRepository<Message, String> {
    fun findByReceiverId(receiverId: String): List<Message>
    fun findBySenderId(senderId: String): List<Message>
    fun findByReceiverIdAndReadFalse(receiverId: String): List<Message>
    fun findBySenderIdAndReceiverId(senderId: String, receiverId: String): List<Message>
} 