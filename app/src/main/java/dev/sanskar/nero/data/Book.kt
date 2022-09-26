package dev.sanskar.nero.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.ceil
import kotlin.math.floor

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val authors: List<String> = emptyList(),
    val publisher: String = "",
    val publishedDate: String = "",
    val currentPage: Int = 1,
    val pageCount: Int = -1,
    val isbn: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val thumbnail: String = "",
    val averageRating: Double = 0.0,
    val ratingsCount: Int = 0,
)

val Book.progress: Float // Remove any fractional part when calculating for neater UI
get() = if (pageCount == -1 || currentPage == 1) 0f else currentPage.toFloat() / pageCount

val Book.publishingDetails: String
get() {
    var details = ""
    if (publisher.isNotEmpty()) {
        details = "Published by $publisher"
    }
    if (publishedDate.isNotEmpty()) {
        if (publisher.isEmpty()) {
            details = publishedDate
        } else {
            details += ", $publishedDate"
        }
    }
    return details
}