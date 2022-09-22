package dev.sanskar.nero.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Stats: Screen("stats", "Stats", Icons.Default.Analytics)
    object BookDetail : Screen("detail", "Book Details", null)
}
