package org.example.ecoswapbackend

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.example.ecoswapbackend.item.model.Item
import org.example.ecoswapbackend.item.model.ItemRepository
import org.example.ecoswapbackend.user.User
import org.example.ecoswapbackend.user.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.math.BigDecimal

@Configuration
class DataLoader {

    @Bean
    fun createUsers(userRepository: UserRepository) = CommandLineRunner {
        userRepository.deleteAll();
        val users = listOf(
            User(id= "1",email = "jdoe@example.com", password= "jdoe123",username = "jdoe", firstName = "John", lastName = "Doe", points = 120),
            User(id= "2",email = "asmith@example.com", password= "asmith123",username = "asmith", firstName = "Anna", lastName = "Smith", points = 80),
            User(id= "3",email = "mmeyer@example.com", password= "mmeyer123",username = "mmeyer", firstName = "Maria", lastName = "Meyer", points = 50),
            User(id= "4",email = "rnowak@example.com", password= "rnowak123",username = "rnowak", firstName = "Robert", lastName = "Nowak", points = 200),
            User(id= "5",email = "klee@example.com", password= "klee123",username = "klee", firstName = "Karl", lastName = "Lee", points = 30)
        )

        userRepository.saveAll(users)
    }

    @Bean
    fun loadItems(itemRepository: ItemRepository) = CommandLineRunner {
        if (itemRepository.count() == 0L) {
            val mapper = jacksonObjectMapper()
            val resource = ClassPathResource("ecoswap.items.json")
            val items: List<Item> = mapper.readValue(resource.inputStream)
            itemRepository.saveAll(items)
        }
    }
}