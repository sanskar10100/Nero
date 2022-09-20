package dev.sanskar.nero.db

import androidx.room.RoomDatabase

abstract class NeroDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun progressDao(): ProgressDao
}