package dev.sanskar.nero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.sanskar.nero.network.GoogleBooksService
import dev.sanskar.nero.ui.theme.NeroTheme
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var googleBooksService: GoogleBooksService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            try {
                val response = googleBooksService.getBooks("androids chet haase")
                Timber.d("Response: $response")
                setContent {
                    Column {
                        response.items.forEach {
                            Text(it.volumeInfo.title)
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.d("Exception: $e")
                e.printStackTrace()
            }
        }
    }
}