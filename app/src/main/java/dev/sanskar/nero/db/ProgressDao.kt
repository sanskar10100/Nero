package dev.sanskar.nero.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.sanskar.nero.data.Progress

@Dao
interface ProgressDao {

    @Insert
    suspend fun insertProgress(progress: Progress)

    @Query("SELECT * FROM progress WHERE bookId = :bookId")
    suspend fun getProgress(bookId: String): List<Progress>
}