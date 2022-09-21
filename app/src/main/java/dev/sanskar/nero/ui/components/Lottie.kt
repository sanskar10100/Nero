package dev.sanskar.nero.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.sanskar.nero.R

@Composable
fun EmptyLottie(text: String = "Add a book to get started!", modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.empty)).value,
            iterations = LottieConstants.IterateForever
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
        )
    }
}

@Composable
fun UnderConstructionLottie(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.under_construction)).value,
            iterations = LottieConstants.IterateForever
        )
    }
}