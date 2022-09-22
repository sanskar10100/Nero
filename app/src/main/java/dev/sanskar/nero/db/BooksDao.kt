package dev.sanskar.nero.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.sanskar.nero.data.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(book: Book)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBooks(books: List<Book>)

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookOneShot(id: String): Book?

    @Query("SELECT * FROM books WHERE id = :id")
    fun getBook(id: String): Flow<Book?>

    @Query("SELECT * FROM books")
    fun getBooks(): Flow<List<Book>>

    @Update
    suspend fun updateBook(book: Book)
}