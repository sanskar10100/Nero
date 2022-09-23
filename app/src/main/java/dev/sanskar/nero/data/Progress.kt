package dev.sanskar.nero.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.sanskar.nero.util.toDayAndMonthAndYear

@Entity
data class Progress(
    @PrimaryKey(autoGenerate = true) val progressId: Int,
    val bookId: String,
    val pagesRead: Int,
    val minutesRead: Int,
    val date: Long,
)

fun List<Progress>.pagesReadForLastSevenDays() = this
    .groupBy { it.date.toDayAndMonthAndYear() }
    .map {
        Pair(it.key.dropLast(4), it.value.sumOf { it.pagesRead })
    }
    .takeLast(7)