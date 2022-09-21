package dev.sanskar.nero.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.sanskar.nero.R
import dev.sanskar.nero.data.Book
import dev.sanskar.nero.ui.theme.NeroTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookRow(
    book: Book,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val animatedAlpha by rememberInfiniteTransition().animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    var imageLoaded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.thumbnail,
                contentDescription = "Book Thumbnail",
                modifier = Modifier
                    .heightIn(128.dp)
                    .width(128.dp),
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.error),
                onError = { imageLoaded = true },
                onSuccess = { imageLoaded = true },
                alpha = if (imageLoaded) 1f else animatedAlpha,
                contentScale = ContentScale.Crop,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = book.authors.joinToString(", "),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = if (book.pageCount == -1) 0f else book.currentPage.toFloat() / book.pageCount,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookRowPreview() {
    NeroTheme {
        BookRow(
            Book(
                "1",
                "A Brief History of Time",
                subtitle = "Let's find out more about the universe",
                description = "A monumental popular science book written by physicist Stephen Hawking that helps you learn how the universe works.",
                authors = listOf("Stephen Hawking"),
                publisher = "Bantam Books",
                publishedDate = "1988",
                currentPage = 1,
                pageCount = 256,
                isbn = listOf("0553380168", "9780553380163"),
                categories = listOf("Non-fiction", "Science"),
                thumbnail = "http://books.google.com/books/content?id=3OTPMeElnW0C&printsec=frontcover&img=1&zoom=1&source=gbs_api",
                averageRating = 5.0,
                ratingsCount = 1,
            )
        ) {

        }
    }
}