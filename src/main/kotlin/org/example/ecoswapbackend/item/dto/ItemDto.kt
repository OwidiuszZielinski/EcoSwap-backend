package org.example.ecoswapbackend.item.dto

import org.bson.types.ObjectId
import org.example.ecoswapbackend.item.model.Category
import org.example.ecoswapbackend.item.model.Condition
import org.example.ecoswapbackend.item.model.Item
import java.math.BigDecimal
import java.time.Instant

data class ItemDto(
    val id: String?,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val category: Category,
    val condition: Condition,
    val ownerId: String?,
    val photoDataUrl: String,
    val available: Boolean,
    val createdAt: Instant,
    val hotDeal: Boolean,
)

fun toEntity(dto: ItemDto): Item {
    return Item(
        id = ObjectId.get(),
        title = dto.title,
        description = dto.description,
        price = dto.price,
        category = dto.category,
        condition = dto.condition,
        photoBase64 = dto.photoDataUrl.substringAfter(","),
        ownerId = dto.ownerId,
        available = dto.available,
        createdAt = dto.createdAt,
        hotDeal = dto.hotDeal,
    )

}

data class ItemResponse(
    val id: String?,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val category: Category,
    val condition: Condition,
    val ownerId: String?,
    val createdAt: Instant,
    val hotDeal: Boolean,
)

fun toItemResponse(dto: ItemDto): ItemResponse {
    return ItemResponse(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        price = dto.price,
        category = dto.category,
        condition = dto.condition,
        ownerId = dto.ownerId,
        createdAt = dto.createdAt,
        hotDeal = dto.hotDeal,
    )
}
