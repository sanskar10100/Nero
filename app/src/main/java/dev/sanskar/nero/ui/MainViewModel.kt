package dev.sanskar.nero.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var searchQuery by mutableStateOf("")
    val googleBooksSearchResult = mutableStateListOf<Book>()

    private var searchJob: Job? = null

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
        viewModelScope.launch {
            bookDao.insertBook(book)
        }
    }

    fun searchGoogleBooks(query: String) {
        searchJob?.cancel()
        searchQuery = query
        searchJob = viewModelScope.launch {
            val result = networkResult { api.getBooks(query) }
            if (result is UiState.Success) {
                googleBooksSearchResult.replaceWith(result.data.items.map { it.toBook() })
            }
        }
    }

    fun clearSearchState() {
        searchQuery = ""
        googleBooksSearchResult.clear()
    }
}