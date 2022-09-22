package dev.sanskar.nero.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.sanskar.nero.data.Book
import dev.sanskar.nero.ui.components.BookRow
import dev.sanskar.nero.ui.components.EmptyLottie

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    onBookClicked: (String) -> Unit
) {
    val finishedBooks by derivedStateOf { viewModel.books.filter { it.currentPage == it.pageCount } }
    val currentlyReadingBooks by derivedStateOf { viewModel.books.filter { it.currentPage > 1 && it.currentPage < it.pageCount } }
    val notStartedBooks by derivedStateOf { viewModel.books.filter { it.currentPage == 1 } }
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
    ) {
        if (viewModel.books.isEmpty()) {
            item {
                EmptyLottie()
            }
        }
        if (currentlyReadingBooks.isNotEmpty()) {
            item {
                Text(
                    text = "Currently Reading",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            items(currentlyReadingBooks) { book ->
                BookRow(book) { onBookClicked(book.id) }
            }
        }
        if (notStartedBooks.isNotEmpty()) {
            item {
                Text(
                    text = "Not Started Yet",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            items(notStartedBooks) { book ->
                BookRow(book) { onBookClicked(book.id) }
            }
        }
        if (finishedBooks.isNotEmpty()) {
            item {
                Text(
                    text = "Finished",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            items(finishedBooks) { book ->
                BookRow(book) { onBookClicked(book.id) }
            }
        }
    }
}