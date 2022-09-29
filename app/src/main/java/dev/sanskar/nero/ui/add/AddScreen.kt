package dev.sanskar.nero.ui.add

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import dev.sanskar.nero.R
import dev.sanskar.nero.data.Book
import dev.sanskar.nero.ui.components.BookRow
import dev.sanskar.nero.util.PickImage
import dev.sanskar.nero.util.clickWithRipple

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    viewModel: AddViewModel = hiltViewModel(),
    onSelected: () -> Unit
) {
    AnimatedContent(
        targetState = viewModel.isAddingCustomBook,
        transitionSpec = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Up) +
                    fadeIn() with slideOutOfContainer(AnimatedContentScope.SlideDirection.Down) + fadeOut()
        },
        modifier = modifier
    ) {
        if (it) {
            AddCustomBookContent(viewModel, onSelected = onSelected)
            BackHandler {
                viewModel.isAddingCustomBook = false
            }
        } else {
            SearchContent(viewModel, onSelected = onSelected) {
                viewModel.isAddingCustomBook = true
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
    var categories by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf("") }

    var showImagePicker by remember { mutableStateOf(false) }

    if (showImagePicker) {
        PickImage {
            showImagePicker = false
            imageUri = it
        }
    }

    val showAddButton by remember {
        derivedStateOf {
            imageUri.isNotEmpty() &&
            bookName.isNotEmpty() &&
            authorName.isNotEmpty() &&
            totalPageCount.isNotEmpty() &&
            totalPageCount.toIntOrNull() != null &&
            categories.isNotEmpty()
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
                .padding(vertical = 8.dp),
            singleLine = true,
            placeholder = { Text("e.g. A Brief History of Time") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = authorName,
            onValueChange = { authorName = it },
            label = { Text("Author Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            placeholder = { Text("Separate multiple authors by commas") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = totalPageCount,
            onValueChange = { totalPageCount = it },
            label = { Text("Total Page Count") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            placeholder = { Text("e.g. 492") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = categories,
            onValueChange = { categories = it },
            label = { Text("Categories") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            placeholder = { Text("Separate multiple categories by commas") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        AnimatedVisibility(showAddButton) {
            Button(
                onClick = {
                    viewModel.addCustomBook(
                        Book(
                            title = bookName,
                            authors = authorName.split(","),
                            pageCount = totalPageCount.toInt(),
                            categories = categories.split(","),
                            thumbnail = imageUri
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