package dev.sanskar.nero.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddBook(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.padding(32.dp)) {
        Text("Add Sample Data")
    }
}