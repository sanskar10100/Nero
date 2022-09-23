package dev.sanskar.nero.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import timber.log.Timber

fun <T> oneShotFlow() = MutableSharedFlow<T>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

@Composable
fun startAnimationOnAdd(): Boolean {
    var state by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        state = true
    }
    return state
}

suspend fun <T> networkResult(call: suspend () -> T): UiState<T> {
    return try {
        val result = call()
        Timber.d("Network result: $result")
        UiState.Success(result)
    } catch (e: Exception) {
        e.printStackTrace()
        Timber.d("Network call failed for $call, exception: $e, message: ${e.message}")
        if (e is HttpException) {
            UiState.Error("An expected error occurred, code: ${e.code()}")
        } else {
            UiState.Error("An unexpected error occurred")
        }
    }
}