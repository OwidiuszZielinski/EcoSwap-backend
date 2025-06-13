package org.example.ecoswapbackend.message

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/messages")
class MessageController(private val messageRepository: MessageRepository) {

    @PostMapping
    fun sendMessage(@RequestBody message: Message): ResponseEntity<MessageDto> {
        val savedMessage = messageRepository.save(message)
        return ResponseEntity.ok(savedMessage.toDto())
    }

    @GetMapping("/received/{userId}")
    fun getReceivedMessages(@PathVariable userId: String): ResponseEntity<List<MessageDto>> {
        val messages = messageRepository.findByReceiverId(userId)
        return ResponseEntity.ok(messages.map { it.toDto() })
    }

    @GetMapping("/sent/{userId}")
    fun getSentMessages(@PathVariable userId: String): ResponseEntity<List<MessageDto>> {
        val messages = messageRepository.findBySenderId(userId)
        return ResponseEntity.ok(messages.map { it.toDto() })
    }

    @GetMapping("/unread/{userId}")
    fun getUnreadMessages(@PathVariable userId: String): ResponseEntity<List<MessageDto>> {
        val messages = messageRepository.findByReceiverIdAndReadFalse(userId)
        return ResponseEntity.ok(messages.map { it.toDto() })
    }

    @GetMapping("/conversation/{userId1}/{userId2}")
    fun getConversation(
        @PathVariable userId1: String,
        @PathVariable userId2: String
    ): ResponseEntity<List<MessageDto>> {
        val messages1 = messageRepository.findBySenderIdAndReceiverId(userId1, userId2)
        val messages2 = messageRepository.findBySenderIdAndReceiverId(userId2, userId1)
        val allMessages = (messages1 + messages2).sortedBy { it.timestamp }
        return ResponseEntity.ok(allMessages.map { it.toDto() })
    }

    @PatchMapping("/{messageId}/read")
    fun markAsRead(@PathVariable messageId: String): ResponseEntity<MessageDto> {
        return messageRepository.findById(messageId)
            .map { message ->
                val updatedMessage = message.copy(read = true)
                val savedMessage = messageRepository.save(updatedMessage)
                ResponseEntity.ok(savedMessage.toDto())
            }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{messageId}")
    fun deleteMessage(@PathVariable messageId: String): ResponseEntity<Unit> {
        return if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId)
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
} 