package dev.sanskar.nero.db

import androidx.room.Dao
import dev.sanskar.nero.data.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    fun insertBook(book: Book)

    fun getBookOneShot(id: String): Book?

    fun getBook(id: String): Flow<Book?>

    fun getBooks(): Flow<List<Book>>
}