package dev.sanskar.nero.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: GoogleBooksService,
    private val bookDao: BookDao,
) : ViewModel() {

    val books = mutableStateListOf<Book>()

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            bookDao.getBooks().collect {
                books.replaceWith(it)
            }
        }
    }

    fun addSample() {
        viewModelScope.launch {
            val result = networkResult { api.getBooks("androids") }
            if (result is UiState.Success) {
                bookDao.insertBooks(result.data.items.map { it.toBook() })
            }
        }
    }
}