package net.pettip.app.navi

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.pettip.app.withClick
import net.pettip.ui.theme.APPTheme
import net.pettip.util.Log
import net.pettip.util.getMethodName

class MainActivity : ComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private fun openMap(context: Context) {
        val intent = Intent(context, MapActivity::class.java)
        startActivity(intent)
    }

    private fun openGpx(context: Context) {
        val intent = Intent(context, GpxActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onResume()
        //if (!application.start) {
        //    openMap(this)
        //}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onCreate(savedInstanceState)
        setContent()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun setContent() {
        //Log.v(__CLASSNAME__, "${getMethodName()}...")
        setContent {
            Content()
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun SetContent() {
        Content()
    }

    @Composable
    fun Content() {
        val context = LocalContext.current
        val shape = RoundedCornerShape(20.0.dp)
        val border = 0.5.dp
        val padding = 20.0.dp
        val modifier = Modifier
            .fillMaxSize()
            .padding(20.0.dp)
            .clip(shape = shape)
            .border(
                width = border,
                color = MaterialTheme.colorScheme.outline,
                shape = shape,
            )
        APPTheme {
            Text(text = "APPTheme", modifier = Modifier.padding(horizontal = padding))
            // A surface container using the 'background' color from the theme
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Text(text = "Surface", modifier = Modifier.padding(horizontal = padding), color = Color.Red)
                }
            }
            Surface(
                modifier = modifier
                    //.padding(20.0.dp)
                    .clickable(onClick = withClick(context) {}),
                border = BorderStroke(0.9.dp, MaterialTheme.colorScheme.outline),
            ) {
                Text(text = "Surface", modifier = Modifier.padding(horizontal = padding))
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = modifier
                        //.padding(20.0.dp)
                        //.border(
                        //    width = border,
                        //    color = MaterialTheme.colorScheme.outline,
                        //    shape = shape,
                        //)
                        .clickable {},
                ) {
                    Text(text = "Box1", modifier = Modifier.padding(horizontal = padding))
                }
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = modifier
                        .padding(20.0.dp)
                        .border(
                            width = border,
                            color = MaterialTheme.colorScheme.outline,
                            shape = shape,
                        )
                        .clickable { },
                ) {
                    Text(text = "Box2", modifier = Modifier.padding(horizontal = padding))
                    Row(
                        modifier = modifier
                            .clickable {},
                        //verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Row", modifier = Modifier
                                .padding(horizontal = padding)
                                .weight(0.19f)
                        )
                        Column(
                            modifier = modifier
                                .weight(0.9f)
                                .clickable {},
                            //verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Column", modifier = Modifier.padding(horizontal = padding))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = padding),
                                onClick = withClick { openMap(context) }) {
                                //OutlinedTextField(value = "good", onValueChange = {})
                                Text(text = stringResource(id = R.string.ok))
                            }
                            Divider(modifier = Modifier.padding(horizontal = padding))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = padding),
                                onClick = withClick { openGpx(context) }) {
                                //OutlinedTextField(value = "bad", onValueChange = {})
                                Text(text = stringResource(id = R.string.cancel))
                            }
                        } //Row
                    }   //Column
                } //Box
            }   //Surface
        }   //APPTheme
    }
}