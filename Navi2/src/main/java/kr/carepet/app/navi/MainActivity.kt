package kr.carepet.app.navi

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.carepet.app.ComponentActivity
import kr.carepet.app.navi.ui.theme.AndTheme
import kr.carepet.util.Log
/**import kr.carepet.util.__CLASSNAME__*/
import kr.carepet.util.getMethodName

open class MainActivity : ComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(__CLASSNAME__, "${getMethodName()}$savedInstanceState")
        super.onCreate(savedInstanceState)
        //setContent { GreetingPreview() }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Greeting("Android")
        }
    }
}
