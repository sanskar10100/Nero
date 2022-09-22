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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val booksDao: BooksDao,
) : ViewModel() {

    var book by mutableStateOf(Book())

    fun getBook(bookId: String) {
        viewModelScope.launch {
            booksDao.getBook(bookId).collect {
                if (it != null) book = it
                else throw IllegalArgumentException("Book not found")
            }
        }
    }

    fun updateProgress(bookId: String, current: Int, total: Int) {
        viewModelScope.launch {
            val book = booksDao.getBookOneShot(bookId) ?: return@launch
            booksDao.updateBook(book.copy(currentPage = current, pageCount = total))
        }
    }
}