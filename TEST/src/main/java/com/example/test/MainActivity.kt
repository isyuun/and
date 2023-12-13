package com.example.test

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.test.ui.theme.AndTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            AndTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier.clickable(onClick = withClick(context) {}),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting("Android")
                        Button(onClick = withClick { }) {
                            Text(text = "Hell Android!")
                        }
                    }
                }
            }
        }
    }
}

/**
 * How to play the platform CLICK sound in Jetpack Compose Button click
 * @see <a href="https://stackoverflow.com/questions/66080018/how-to-play-the-platform-click-sound-in-jetpack-compose-button-click">How to play the platform CLICK sound in Jetpack Compose Button click</a>
 */
fun withClick(context: Context, onClick: () -> Unit): () -> Unit {
    return {
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).playSoundEffect(AudioManager.FX_KEY_CLICK)
        onClick()
    }
}

/**
 * How to play the platform CLICK sound in Jetpack Compose Button click
 * @see <a href="https://stackoverflow.com/questions/66080018/how-to-play-the-platform-click-sound-in-jetpack-compose-button-click">How to play the platform CLICK sound in Jetpack Compose Button click</a>
 */
@Composable
fun withClick(onClick: () -> Unit): () -> Unit {
    val context = LocalContext.current
    return {
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).playSoundEffect(AudioManager.FX_KEY_CLICK)
        onClick()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hell $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndTheme {
        Greeting("Android")
    }
}