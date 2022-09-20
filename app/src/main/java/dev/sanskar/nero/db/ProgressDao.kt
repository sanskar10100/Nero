package dev.sanskar.nero.db

import androidx.room.Dao
import dev.sanskar.nero.data.Progress

@Dao
interface ProgressDao {

    fun insertProgress(progress: Progress)

    fun getProgress(bookId: String): List<Progress>
}