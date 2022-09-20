package dev.sanskar.nero.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val authors: List<String>,
    val publisher: String,
    val publishedDate: String,
    val currentPage: Int,
    val pageCount: Int,
    val isbn: List<String>,
    val categories: List<String>,
    val thumbnail: String,
    val averageRating: Double,
    val ratingsCount: Int,
)