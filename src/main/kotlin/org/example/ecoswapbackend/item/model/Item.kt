package org.example.ecoswapbackend.item.model

import org.bson.types.ObjectId
import org.example.ecoswapbackend.item.dto.ItemDto
import org.example.ecoswapbackend.item.dto.ItemResponse
import org.example.ecoswapbackend.item.dto.toEntity
import org.example.ecoswapbackend.item.dto.toItemResponse
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant

@Document(collection = "items")
data class Item(
    @Id var id: ObjectId = ObjectId(),
    val title: String,
    val description: String,
    val price: BigDecimal,
    val category: Category,
    val condition: Condition = Condition.GOOD,
    val photoBase64: String,
    val ownerId: String?,
    val available: Boolean = true,
    val createdAt: Instant? = Instant.now(),
    val hotDeal: Boolean = false,
)

enum class Category { ELECTRONICS, SPORT, CHILDREN, HOME, OTHER }
enum class Condition { NEW, VERY_GOOD, GOOD, OK, DAMAGED }

fun Item.toDto() = ItemDto(
    id = id.toString(),
    title = title,
    description = description,
    price = price,
    category = category,
    condition = condition,
    ownerId = ownerId,
    photoDataUrl = photoBase64.substringAfter(","),
    available = available,
    createdAt = createdAt,
    hotDeal = hotDeal
)

interface ItemRepository : MongoRepository<Item, String> {
    fun findByHotDealTrue(): List<Item>
    fun findByOwnerId(ownerId: String): List<Item>
}

@RestController
@RequestMapping("/api/items")
class ItemController(private val repo: ItemRepository) {

    @GetMapping
    fun getAll(): List<ItemDto> {
        return repo.findAll().map { it.toDto() }
    }

    @GetMapping
    @RequestMapping("/deals")
    fun getDeals(): List<ItemDto> {
        return repo.findByHotDealTrue()
            .map { it.toDto() }
    }

    @GetMapping("/owner/{ownerId}")
    fun getByOwnerId(@PathVariable ownerId: String): List<ItemDto> {
        return repo.findByOwnerId(ownerId).map { it.toDto() }
    }

    @PostMapping
    fun create(@RequestBody body: ItemDto): ItemResponse? {
        repo.save(toEntity(body))
        return toItemResponse(body)
    }

    @DeleteMapping("/{id}")
    fun deleteItem(@PathVariable id: String): ResponseEntity<Unit> {
        return if (repo.existsById(id)) {
            repo.deleteById(id)
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateItem(@PathVariable id: String, @RequestBody body: ItemDto): ResponseEntity<ItemResponse> {
        return if (repo.existsById(id)) {
            val updatedItem = toEntity(body).copy(id = ObjectId(id))
            repo.save(updatedItem)
            ResponseEntity.ok(toItemResponse(body))
        } else {
            ResponseEntity.notFound().build()
        }
    }
}