package dev.sanskar.nero.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sanskar.nero.data.Book
import dev.sanskar.nero.data.Progress
import dev.sanskar.nero.util.Converters

@Database(entities = [Book::class, Progress::class], version = 1)
@TypeConverters(Converters::class)
abstract class NeroDatabase : RoomDatabase() {
    abstract fun bookDao(): BooksDao
    abstract fun progressDao(): ProgressDao
}