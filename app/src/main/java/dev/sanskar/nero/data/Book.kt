package dev.sanskar.nero.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val authors: List<String> = emptyList(),
    val publisher: String = "",
    val publishedDate: String = "",
    val currentPage: Int = 0,
    val pageCount: Int = 0,
    val isbn: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val thumbnail: String = "",
    val averageRating: Double = 0.0,
    val ratingsCount: Int = 0,
)