package dev.sanskar.nero.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.sanskar.nero.data.Progress
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    @Insert
    suspend fun insertProgress(progress: Progress)

    @Query("SELECT * FROM progress WHERE bookId = :bookId")
    fun getProgress(bookId: String): Flow<List<Progress>>
}