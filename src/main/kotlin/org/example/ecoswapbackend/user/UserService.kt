package org.example.ecoswapbackend.user

import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<User> = userRepository.findAll()

    fun getUserById(id: String): User? = userRepository.findById(id).orElse(null)

    fun getUserByUsername(username: String): User? = userRepository.findByUsername(username)

    fun createUser(user: User): User = userRepository.save(user)

    fun updateUser(id: String, updatedUser: User): User? {
        return userRepository.findById(id).map { existingUser ->
            val userToUpdate = existingUser.copy(
                firstName = updatedUser.firstName,
                lastName = updatedUser.lastName,
                points = updatedUser.points
            )
            userRepository.save(userToUpdate)
        }.orElse(null)
    }

    fun deleteUser(id: String) = userRepository.deleteById(id)

    fun updatePoints(id: String, points: Int): User? {
        return userRepository.findById(id).map { user ->
            val updatedUser = user.copy(points = points)
            userRepository.save(updatedUser)
        }.orElse(null)
    }
} 