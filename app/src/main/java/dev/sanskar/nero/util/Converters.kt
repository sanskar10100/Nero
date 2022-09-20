package dev.sanskar.nero.util

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringToList(value: String) = value.split(",").map { it.trim() }

    @TypeConverter
    fun fromListToString(list: List<String>) = list.joinToString(separator = ",")
}