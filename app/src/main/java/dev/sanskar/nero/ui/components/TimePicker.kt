package dev.sanskar.nero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import dev.sanskar.nero.ui.theme.NeroTheme
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun VerticalNumberPicker(
    list: List<Int>,
    modifier: Modifier = Modifier,
    onSelectedIndex: (Int) -> Unit,
    textColor: Color = MaterialTheme.colors.onSurface,
    selectedTextColor: Color = MaterialTheme.colors.primary,
) {
    val lazyListState = rememberLazyListState(list.size - 1)
    val layoutInfo = rememberLazyListSnapperLayoutInfo(lazyListState)

    val scope = rememberCoroutineScope()

    val itemIsSelected = { index: Int ->
        onSelectedIndex((layoutInfo.currentItem?.index ?: -1) % list.size)
        ((layoutInfo.currentItem?.index ?: -1) % list.size) == (index % list.size)
    }
    LazyColumn(
        state = lazyListState,
        flingBehavior = rememberSnapperFlingBehavior(layoutInfo),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(Int.MAX_VALUE) {
            Text(
                text = list[it % list.size].toString(),
                fontSize = 36.sp,
                fontWeight = if (itemIsSelected(it)) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                },
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            lazyListState.animateScrollToItem(it - 1)
                        }
                    },
                color = if (itemIsSelected(it)) selectedTextColor else textColor
            )
        }
    }
}

@Composable
fun TimePicker(modifier: Modifier = Modifier, onSelectedIndex: (Int, Int) -> Unit) {
    Surface(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(0.7f),
        shape = RoundedCornerShape(4.dp),
        elevation = 3.dp
    ) {
        Box {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(177.dp)
            ) {
                val hourOptions = remember { (0..99).toList() }
                val minuteOptions = remember { (0..59).toList() }
                var selectedHourIndex = remember { 0 }
                var selectedMinuteIndex = remember { 0 }

                VerticalNumberPicker(
                    list = hourOptions,
                    modifier = Modifier
                        .weight(1f),
                    onSelectedIndex = {
                        selectedHourIndex = it
                        onSelectedIndex(selectedHourIndex, selectedMinuteIndex)
                    }
                )

                VerticalNumberPicker(
                    list = minuteOptions,
                    modifier = Modifier
                        .weight(1f),
                    onSelectedIndex = {
                        selectedMinuteIndex = it
                        onSelectedIndex(selectedHourIndex, selectedMinuteIndex)
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .align(Alignment.Center),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .alpha(0.2f)
                        .background(MaterialTheme.colors.primary)
                )
                Text(
                    text = ":",
                    fontSize = 36.sp,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-4).dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun TimePickerPreview() {
    NeroTheme {
        TimePicker(onSelectedIndex = { hour, minute ->
            Timber.d("Selected hour: $hour, minute: $minute")
        })
    }
}

@Preview
@Composable
fun VerticalNumberPickerPreview() {
    NeroTheme {
        val numbers = remember { (0..99).toList() }
        var selected = remember { 0 }
        VerticalNumberPicker(
            list = numbers,
            onSelectedIndex = {
                Timber.d("Selected index: $it")
                selected = it
            }
        )
    }
}