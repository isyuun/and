package net.pettip.app.navi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import net.pettip.app.navi.ui.theme.AppTheme
import net.pettip.gps.R
import net.pettip.gps.app.GPSApplication
import net.pettip.util.Log
import net.pettip.util.getMethodName

class MainActivity : ComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    val application = GPSApplication.instance

    private fun openMap(context: Context) {
        val intent = Intent(context, MapActivity::class.java)
        startActivity(intent)
    }

    private fun openGpx(context: Context) {
        val intent = Intent(context, GpxActivity::class.java)
        startActivity(intent)
    }

    private fun setContent() {
        setContent {
            val context = LocalContext.current
            AppTheme(dynamicColor = true) {
                Surface {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Button(onClick = { openMap(context) }) {
                            //OutlinedTextField(value = "good", onValueChange = {})
                            Text(text = stringResource(id = R.string.start))
                        }
                        Button(onClick = { openGpx(context) }) {
                            //OutlinedTextField(value = "bad", onValueChange = {})
                            Text(text = stringResource(id = R.string.start))
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.start}]")
        super.onCreate(savedInstanceState)
        setContent()
    }

    override fun onStart() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.start}]")
        super.onStart()
    }

    override fun onResume() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.start}]")
        super.onResume()
        //if (!application.start) {
        //    openMap(this)
        //}
    }

    override fun onNewIntent(intent: Intent?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.start}]")
        super.onNewIntent(intent)
    }
}