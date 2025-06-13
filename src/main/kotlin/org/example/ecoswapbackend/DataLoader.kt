package org.example.ecoswapbackend

import org.example.ecoswapbackend.user.User
import org.example.ecoswapbackend.user.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataLoader {

    @Bean
    fun createUsers(userRepository: UserRepository) = CommandLineRunner {
        val users = listOf(
            User(username = "jdoe", firstName = "John", lastName = "Doe", points = 120),
            User(username = "asmith", firstName = "Anna", lastName = "Smith", points = 80),
            User(username = "mmeyer", firstName = "Maria", lastName = "Meyer", points = 50),
            User(username = "rnowak", firstName = "Robert", lastName = "Nowak", points = 200),
            User(username = "klee", firstName = "Karl", lastName = "Lee", points = 30)
        )

        userRepository.saveAll(users)
    }
}