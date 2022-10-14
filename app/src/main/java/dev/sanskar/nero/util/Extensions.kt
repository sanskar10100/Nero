package dev.sanskar.nero.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Modifier.clickWithRipple(bounded: Boolean = true, onClick: () -> Unit) = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(bounded = bounded),
        onClick = { onClick() }
    )
}

fun <T> SnapshotStateList<T>.replaceWith(list: List<T>) {
    this.clear()
    this.addAll(list)
}

fun Long.toDayAndMonthAndYear(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(date)
}

fun Int.toHourAndMinute(): String {
    val hour = this / 60
    val minute = this % 60
    return "$hour h $minute m"
}

infix fun Long.sameDateAs(date: Long): Boolean {
    val thisDate = Date(this)
    val otherDate = Date(date)
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(thisDate) == format.format(otherDate)
}