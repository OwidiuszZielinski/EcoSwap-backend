package org.example.ecoswapbackend.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> = 
        ResponseEntity.ok(userService.getAllUsers())

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<User> =
        userService.getUserById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @GetMapping("/username/{username}")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<User> =
        userService.getUserByUsername(username)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<User> =
        ResponseEntity.ok(userService.createUser(user))

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody updatedUser: User): ResponseEntity<User> =
        userService.updateUser(id, updatedUser)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{id}/points")
    fun updatePoints(@PathVariable id: String, @RequestParam points: Int): ResponseEntity<User> =
        userService.updatePoints(id, points)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
} 