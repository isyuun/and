/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.20
 *
 */

package net.pettip._test.app.navi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.pettip.app.withClick
import net.pettip.ui.theme.APPTheme
import net.pettip.ui.theme.IFCommBlue
import net.pettip.ui.theme.IFCommGreen
import net.pettip.ui.theme.IFCommRed
import net.pettip.ui.theme.IFCommYellow
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
            SetContent()
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun SetContent() {
        APPTheme {
            Content()
        }
    }

    @Composable
    fun Content() {
        val context = LocalContext.current
        val shape = RoundedCornerShape(20.0.dp)
        val width = 0.5.dp
        val padding = 12.0.dp
        val modifier = Modifier
            .fillMaxSize()
            .padding(20.0.dp)
            .clip(shape = shape)
            .border(
                width = width,
                color = MaterialTheme.colorScheme.outline,
                shape = shape,
            )
            .clickable {}
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(contentAlignment = Alignment.TopEnd) {
                Text(text = "Blank:background", modifier = Modifier.padding(horizontal = padding), color = Color.Red)
            }
        }
        // A surface container using the 'surface'(default) color from the theme
        Surface(
            modifier = modifier.clickable(onClick = withClick(context) {}),
            border = BorderStroke(0.9.dp, Color.Cyan),
        ) {
            Text(text = "Surface:surface", modifier = Modifier.padding(horizontal = padding))
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = modifier
                    .padding(2.0.dp)
                    .border(
                        width = width,
                        color = Color.Red,
                        shape = shape,
                    ),
            ) {
                Text(text = "Box1", modifier = Modifier.padding(horizontal = padding))
            } //Box1
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = modifier
                    .padding(4.0.dp)
                    .border(
                        width = width,
                        color = Color.Yellow,
                        shape = shape,
                    ),
            ) {
                Text(text = "Box2", modifier = Modifier.padding(horizontal = padding))
                //Row(
                //    modifier = modifier,
                //    //verticalAlignment = Alignment.CenterVertically,
                //) {
                //    Text(
                //        text = "Row", modifier = Modifier
                //            .padding(horizontal = padding)
                //            .weight(0.15f)
                //    )
                //    Box(
                //        modifier = modifier
                //            .weight(0.9f),
                //        ) {
                //        AppContent(modifier, padding)
                //    }
                //}   //Row
                AppContent(modifier, padding)
            }   //Box2
        }   //Surface
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart,
        ) {
            var clicked by remember { mutableStateOf(false) }
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(net.pettip.R.string.start),
                        fontSize = 12.sp,
                    )
                },
                icon = { Icon(if (clicked) Icons.Default.Close else Icons.Default.Add, contentDescription = null) },
                onClick = withClick { clicked = !clicked },
                shape = CircleShape,
                expanded = clicked,
            )
            //FloatingActionButton(
            //    onClick = withClick { },
            //    shape = CircleShape,
            //) {
            //    Column(
            //        horizontalAlignment = Alignment.CenterHorizontally,
            //        verticalArrangement = Arrangement.Center
            //    ) {
            //        Icon(
            //            painter = rememberVectorPainter(Icons.Default.Add),
            //            contentDescription = null,
            //        )
            //        Text(
            //            text = stringResource(net.pettip.R.string.start),
            //            fontSize = 12.sp,
            //        )
            //    }
            //}
        }
    }

    private @Composable
    fun AppContent(modifier: Modifier, padding: Dp) {
        val context = LocalContext.current
        var enabled by remember { mutableStateOf(true) }
        Column(
            modifier = modifier
                .padding(8.0.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(padding),
        ) {
            Text(text = "Column", modifier = Modifier.padding(horizontal = padding))
            ColorChips()
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick {
                    Log.i(__CLASSNAME__, "${getMethodName()}$enabled")
                    enabled = !enabled
                    Log.w(__CLASSNAME__, "${getMethodName()}$enabled")
                },
                colors = ButtonDefaults.buttonColors(if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary),
            ) { Text(text = if (enabled) "Btn:primary:enable" else "Btn:inversePrimary:disable", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            Divider()
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { openMap(context) },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                enabled = enabled,
            ) { Text(text = "Btn:primary", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { openGpx(context) },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inversePrimary),
                enabled = enabled,
            ) { Text(text = "Btn:inversePrimary", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                enabled = enabled,
            ) { Text(text = "Btn:secondary", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                enabled = enabled,
            ) { Text(text = "Btn:tertiary", maxLines = 1, overflow = TextOverflow.Ellipsis) }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                colors = ButtonDefaults.buttonColors(IFCommBlue),
                enabled = enabled,
            ) { Text(text = "Btn:IFCommBlue", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                colors = ButtonDefaults.buttonColors(IFCommGreen),
                enabled = enabled,
            ) { Text(text = "Btn:IFCommGreen", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                colors = ButtonDefaults.buttonColors(IFCommYellow),
                enabled = enabled,
            ) { Text(text = "Btn:IFCommYellow", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = withClick { },
                colors = ButtonDefaults.buttonColors(IFCommRed),
                enabled = enabled,
            ) { Text(text = "Btn:IFCommRed", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            TextField(
                value = "TextField", onValueChange = {},
                singleLine = true,
                enabled = enabled,
                isError = false,
            )
            TextField(
                value = "TextField:Error", onValueChange = {},
                singleLine = true,
                enabled = enabled,
                isError = true,
            )
            OutlinedTextField(
                value = "OutlinedTextField", onValueChange = {},
                singleLine = true,
                enabled = enabled,
                isError = false,
            )
            OutlinedTextField(
                value = "OutlinedTextField:Error", onValueChange = {},
                singleLine = true,
                enabled = enabled,
                isError = true,
            )
        } //Row
    }


    fun colorToHexString(color: Color): String {
        // Color을 ARGB 형식의 정수로 변환
        val argb = android.graphics.Color.argb(
            (color.alpha * 255).toInt(),
            (color.red * 255).toInt(),
            (color.green * 255).toInt(),
            (color.blue * 255).toInt()
        )

        // 정수를 16진수 문자열로 변환
        val hexString = Integer.toHexString(argb)

        // "0x"를 앞에 붙여서 반환
        return "0x$hexString".uppercase()
    }

    @Composable
    fun ColorChip(label: String, color: Color, text: Color = MaterialTheme.colorScheme.onPrimary) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .padding(8.dp)
        ) {
            Text(
                text = label,
                color = text,
                maxLines = 1,
                textAlign = TextAlign.Start,
                style = LocalTextStyle.current,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(4.dp),
            )
            Text(
                text = "(${colorToHexString(color)})",
                color = text,
                maxLines = 1,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
    }

    @Composable
    fun ColorChips() {
        ColorChip(label = "primary", color = MaterialTheme.colorScheme.primary, text = MaterialTheme.colorScheme.onPrimary)
        ColorChip(label = "onPrimary", color = MaterialTheme.colorScheme.onPrimary, text = MaterialTheme.colorScheme.primary)
        ColorChip(label = "primaryContainer", color = MaterialTheme.colorScheme.primaryContainer, text = MaterialTheme.colorScheme.onPrimaryContainer)
        ColorChip(label = "onPrimaryContainer", color = MaterialTheme.colorScheme.onPrimaryContainer, text = MaterialTheme.colorScheme.primaryContainer)
        ColorChip(label = "inversePrimary", color = MaterialTheme.colorScheme.inversePrimary, text = MaterialTheme.colorScheme.primary)
        ColorChip(label = "secondary", color = MaterialTheme.colorScheme.secondary, text = MaterialTheme.colorScheme.onSecondary)
        ColorChip(label = "onSecondary", color = MaterialTheme.colorScheme.onSecondary, text = MaterialTheme.colorScheme.secondary)
        ColorChip(label = "secondaryContainer", color = MaterialTheme.colorScheme.secondaryContainer, text = MaterialTheme.colorScheme.onSecondaryContainer)
        ColorChip(label = "onSecondaryContainer", color = MaterialTheme.colorScheme.onSecondaryContainer, text = MaterialTheme.colorScheme.secondaryContainer)
        ColorChip(label = "tertiary", color = MaterialTheme.colorScheme.tertiary, text = MaterialTheme.colorScheme.onTertiary)
        ColorChip(label = "onTertiary", color = MaterialTheme.colorScheme.onTertiary, text = MaterialTheme.colorScheme.tertiary)
        ColorChip(label = "tertiaryContainer", color = MaterialTheme.colorScheme.tertiaryContainer, text = MaterialTheme.colorScheme.onTertiaryContainer)
        ColorChip(label = "onTertiaryContainer", color = MaterialTheme.colorScheme.onTertiaryContainer, text = MaterialTheme.colorScheme.tertiaryContainer)
        ColorChip(label = "background", color = MaterialTheme.colorScheme.background, text = MaterialTheme.colorScheme.onBackground)
        ColorChip(label = "onBackground", color = MaterialTheme.colorScheme.onBackground, text = MaterialTheme.colorScheme.background)
        ColorChip(label = "surface", color = MaterialTheme.colorScheme.surface, text = MaterialTheme.colorScheme.onSurface)
        ColorChip(label = "onSurface", color = MaterialTheme.colorScheme.onSurface, text = MaterialTheme.colorScheme.surface)
        ColorChip(label = "surfaceVariant", color = MaterialTheme.colorScheme.surfaceVariant, text = MaterialTheme.colorScheme.onSurfaceVariant)
        ColorChip(label = "onSurfaceVariant", color = MaterialTheme.colorScheme.onSurfaceVariant, text = MaterialTheme.colorScheme.surfaceVariant)
        ColorChip(label = "surfaceTint", color = MaterialTheme.colorScheme.surfaceTint)
        ColorChip(label = "inverseSurface", color = MaterialTheme.colorScheme.inverseSurface, text = MaterialTheme.colorScheme.inverseOnSurface)
        ColorChip(label = "inverseOnSurface", color = MaterialTheme.colorScheme.inverseOnSurface, text = MaterialTheme.colorScheme.inverseSurface)
        ColorChip(label = "error", color = MaterialTheme.colorScheme.error, text = MaterialTheme.colorScheme.onError)
        ColorChip(label = "onError", color = MaterialTheme.colorScheme.onError, text = MaterialTheme.colorScheme.error)
        ColorChip(label = "errorContainer", color = MaterialTheme.colorScheme.errorContainer, text = MaterialTheme.colorScheme.onErrorContainer)
        ColorChip(label = "onErrorContainer", color = MaterialTheme.colorScheme.onErrorContainer, text = MaterialTheme.colorScheme.errorContainer)
        ColorChip(label = "outline", color = MaterialTheme.colorScheme.outline, text = MaterialTheme.colorScheme.outlineVariant)
        ColorChip(label = "outlineVariant", color = MaterialTheme.colorScheme.outlineVariant, text = MaterialTheme.colorScheme.outline)
        ColorChip(label = "scrim", color = MaterialTheme.colorScheme.onSurface)
    }
}