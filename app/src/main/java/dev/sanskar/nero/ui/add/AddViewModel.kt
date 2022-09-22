package dev.sanskar.nero.ui.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.nero.data.Book
import dev.sanskar.nero.data.toBook
import dev.sanskar.nero.db.BooksDao
import dev.sanskar.nero.network.GoogleBooksService
import dev.sanskar.nero.util.UiState
import dev.sanskar.nero.util.networkResult
import dev.sanskar.nero.util.replaceWith
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@HiltViewModel
class AddViewModel @Inject constructor(
    private val api: GoogleBooksService,
    private val booksDao: BooksDao
) : ViewModel() {
    var searchQuery by mutableStateOf("")
    val googleBooksSearchResult = mutableStateListOf<Book>()


    private var searchJob: Job? = null


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

    fun addBook(book: Book) {
        viewModelScope.launch {
            booksDao.insertBook(book)
        }
    }

    fun clearSearchState() {
        searchQuery = ""
        googleBooksSearchResult.clear()
    }
}