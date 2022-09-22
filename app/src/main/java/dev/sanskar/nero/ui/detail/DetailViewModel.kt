package dev.sanskar.nero.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.nero.data.Book
import dev.sanskar.nero.db.BooksDao
import dev.sanskar.nero.db.ProgressDao
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val booksDao: BooksDao,
    private val progressDao: ProgressDao
) : ViewModel() {

    var book by mutableStateOf(Book())

    fun getBook(bookId: String) {
        viewModelScope.launch {
            book = booksDao.getBookOneShot(bookId) ?: throw java.lang.IllegalArgumentException("Book not found")
        }
    }
}