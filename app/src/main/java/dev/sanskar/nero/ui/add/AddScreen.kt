package dev.sanskar.nero.ui.add

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import dev.sanskar.nero.R
import dev.sanskar.nero.data.Book
import dev.sanskar.nero.ui.components.BookRow
import dev.sanskar.nero.util.clickWithRipple
import dev.sanskar.nero.util.getNewFileUri
import timber.log.Timber

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    viewModel: AddViewModel = hiltViewModel(),
    onSelected: () -> Unit
) {
    var isAddingCustomBook by remember { mutableStateOf(false) }
    AnimatedContent(
        targetState = isAddingCustomBook,
        transitionSpec = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Up) +
                    fadeIn() with slideOutOfContainer(AnimatedContentScope.SlideDirection.Down) + fadeOut()
        }
    ) {
        if (it) {
            AddCustomBookContent(viewModel, onSelected = onSelected)
            BackHandler {
                isAddingCustomBook = false
            }
        } else {
            SearchContent(viewModel, onSelected = onSelected) {
                isAddingCustomBook = true
            }
        }
    }
}

@Composable
fun AddCustomBookContent(
    viewModel: AddViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onSelected: () -> Unit
) {
    var bookName by remember { mutableStateOf("") }
    var authorName by remember { mutableStateOf("") }
    var totalPageCount by remember { mutableStateOf("") }

    val context = LocalContext.current

    var imageUri by remember { mutableStateOf("") }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    var showImagePicker by remember { mutableStateOf(false) }

    val galleryPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        imageUri = it?.toString() ?: ""
        Timber.d("Received URI: $it")
    }


    val cameraPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        imageUri = if (it) {
            cameraUri?.toString() ?: ""
        } else {
            ""
        }
    }

    if (showImagePicker) Dialog(
        onDismissRequest = { showImagePicker = false },
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
                    showImagePicker = false
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
                    showImagePicker = false
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

    val showAddButton by remember {
        derivedStateOf {
            bookName.isNotEmpty() &&
            authorName.isNotEmpty() &&
            totalPageCount.isNotEmpty() &&
            totalPageCount.toIntOrNull() != null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        AsyncImage(
            model = imageUri.ifEmpty { R.drawable.baseline_book_24 },
            contentDescription = "Book Cover",
            contentScale = if (imageUri.isEmpty()) ContentScale.Fit else ContentScale.Crop,
            modifier = Modifier
                .height(300.dp)
                .width(200.dp)
                .padding(16.dp)
                .clickWithRipple {
                    showImagePicker = true
                },
        )
        OutlinedTextField(
            value = bookName,
            onValueChange = { bookName = it },
            label = { Text("Book Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = authorName,
            onValueChange = { authorName = it },
            label = { Text("Author Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = totalPageCount,
            onValueChange = { totalPageCount = it },
            label = { Text("Total Page Count") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        AnimatedVisibility(showAddButton) {
            Button(
                onClick = {
                    viewModel.addCustomBook(
                        Book(
                            title = bookName,
                            authors = authorName.split(","),
                            pageCount = totalPageCount.toInt()
                        )
                    )
                    onSelected()
                },
                enabled = showAddButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Add")
            }
        }
    }
}

@Composable
private fun SearchContent(
    viewModel: AddViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onSelected: () -> Unit,
    onAddCustomBookClicked: () -> Unit = {}
) {
    val focusRequester = FocusRequester()
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally
    ) {
        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = {
                viewModel.searchGoogleBooks(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .focusRequester(focusRequester),
            label = { Text("Search") },
            placeholder = { Text("Book name, author(s) etc") }
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally
        ) {
            if (viewModel.googleBooksSearchResult.isEmpty()) {
                item {
                    Text(
                        "Search for a book\n\nOR",
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                }
            }
            items(viewModel.googleBooksSearchResult, key = { it.id }) {
                BookRow(it, showProgressBar = false) {
                    viewModel.addBook(it)
                    onSelected()
                }
            }
            item {
                Button(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .height(48.dp)
                        .fillMaxWidth(0.8f),
                    onClick = onAddCustomBookClicked
                ) {
                    Text(
                        "Add custom book",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}