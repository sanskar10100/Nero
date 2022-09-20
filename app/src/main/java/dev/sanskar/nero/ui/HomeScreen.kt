package dev.sanskar.nero.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import dev.sanskar.nero.data.Book

@Composable
fun HomeScreen(books: SnapshotStateList<Book>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(books, key = { it.id }) {
            Text(it.title)
        }
    }
}