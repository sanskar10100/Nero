package dev.sanskar.nero.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BookDetail(bookId: String, modifier: Modifier = Modifier) {
    Text("Received book id: $bookId")
}