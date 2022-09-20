package dev.sanskar.nero.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Progress(
    @PrimaryKey(autoGenerate = true) val progressId: Int,
    val bookId: String,
    val pagesRead: Int,
    val timeRead: Long,
    val date: String,
)