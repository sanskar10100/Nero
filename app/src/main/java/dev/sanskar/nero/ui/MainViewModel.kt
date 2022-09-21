package dev.sanskar.nero.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.nero.data.Book
import dev.sanskar.nero.data.toBook
import dev.sanskar.nero.db.BookDao
import dev.sanskar.nero.network.GoogleBooksService
import dev.sanskar.nero.util.UiState
import dev.sanskar.nero.util.networkResult
import dev.sanskar.nero.util.replaceWith
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: GoogleBooksService,
    private val bookDao: BookDao,
) : ViewModel() {

    val books = mutableStateListOf<Book>()
    var searchJob: Job? = null
    val googleBooksSearchResult = mutableStateListOf<Book>()

    init {
        getBooks()
    }

    private fun getBooks() {
        viewModelScope.launch {
            bookDao.getBooks().collect {
                books.replaceWith(it)
            }
        }
    }

    fun addBook(book: Book) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            bookDao.insertBook(book)
        }
    }

    fun searchGoogleBooks(query: String) {
        viewModelScope.launch {
            val result = networkResult { api.getBooks(query) }
            if (result is UiState.Success) {
                googleBooksSearchResult.replaceWith(result.data.items.map { it.toBook() })
            }
        }
    }
}