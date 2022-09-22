package dev.sanskar.nero.ui.detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.nero.db.BooksDao
import dev.sanskar.nero.db.ProgressDao
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val booksDao: BooksDao,
    private val progressDao: ProgressDao
) : ViewModel() {

}