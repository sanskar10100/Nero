package dev.sanskar.nero.util

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import java.io.File
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

fun Context.getNewFileUri(): Uri {
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    val file = File(filesDir, fileName)
    return FileProvider.getUriForFile(this, "dev.sanskar.nero", file)
}

@Composable
fun PickImage(
    onImagePicked: (String) -> Unit,
) {
    val context = LocalContext.current
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    var showOptions by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showOptions = true
    }

    val cameraPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        onImagePicked(if (it) cameraUri.toString() ?: "" else "")
    }

    val galleryPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        onImagePicked(it?.toString() ?: "")
    }

    if (showOptions) Dialog(
        onDismissRequest = { showOptions = false },
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.8f)
                .height(72.dp),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = {
                    showOptions = false
                    cameraUri = context.getNewFileUri()
                    cameraPicker.launch(cameraUri)
                }) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = "Pick Image using Camera",
                        modifier = Modifier
                            .scale(1.5f)
                    )
                }

                IconButton(onClick = {
                    showOptions = false
                    galleryPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) {
                    Icon(
                        imageVector = Icons.Default.PhotoAlbum,
                        contentDescription = "Pick Image using Gallery",
                        modifier = Modifier
                            .scale(1.5f)
                    )
                }
            }
        }
    }
}