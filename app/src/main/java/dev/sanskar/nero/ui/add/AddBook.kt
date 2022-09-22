package dev.sanskar.nero.ui.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.sanskar.nero.R
import dev.sanskar.nero.ui.components.BookRow

@Composable
fun AddBook(
    modifier: Modifier = Modifier,
    viewModel: AddViewModel = hiltViewModel(),
    onSelected: () -> Unit
) {
    val focusRequester = FocusRequester()
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
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
        LazyColumn {
            if (viewModel.googleBooksSearchResult.isEmpty()) {
                item {
                    LottieAnimation(
                        composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.search)).value,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            items(viewModel.googleBooksSearchResult, key = { it.id }) {
                BookRow(it) {
                    viewModel.addBook(it)
                    onSelected()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}