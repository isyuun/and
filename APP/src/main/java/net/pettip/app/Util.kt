/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.11.28
 *
 */

package net.pettip.app

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.delay
import net.pettip.DEBUG
import net.pettip.R
import net.pettip.RELEASE
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @Project     : carepet-android
 * @FileName    : __mapinterface.kt
 * @Date        : 2023. 09. 20.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */

//private val __CLASSNAME__ = Exception().stackTrace[0].fileName

private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

private fun getDrawableWithBackgroundColor(drawable: Drawable, backgroundColor: Int): Drawable {
    val width = drawable.intrinsicWidth
    val height = drawable.intrinsicHeight

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint()
    paint.color = backgroundColor
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

    drawable.setBounds(0, 0, width, height)
    drawable.draw(canvas)

    return BitmapDrawable(Resources.getSystem(), bitmap)
}

private fun getRounded(source: Bitmap, backColor: Int, outlineColor: Int): Bitmap {
    val width = source.width
    val height = source.height

    val padding = Integer.min(width, height) / 4
    val newWidth = width - (padding * 2)
    val newHeight = height - (padding * 2)

    // Create a scaled bitmap with the calculated dimensions
    val scaledBitmap = Bitmap.createScaledBitmap(source, newWidth, newHeight, true)

    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val paint = Paint()
    paint.isAntiAlias = true

    // Draw a solid background with the specified color
    paint.color = backColor
    canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), width.toFloat(), height.toFloat(), paint)

    // Calculate the coordinates to center the scaled bitmap
    val left = (width - newWidth) / 2f
    val top = (height - newHeight) / 2f

    // Draw the rounded image inside the destination rect using scaledBitmap
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(scaledBitmap, left, top, paint)

    // Draw the outline
    paint.xfermode = null
    paint.color = outlineColor
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 0.01f

    val outerRect = RectF(1f, 1f, width.toFloat() - 2, height.toFloat() - 2)
    canvas.drawRoundRect(outerRect, width.toFloat(), height.toFloat(), paint)

    return output
}

fun getRounded(context: Context, id: Int, backColor: Color): Bitmap? {
    val resources = context.resources
    val drawable = ResourcesCompat.getDrawable(resources, id, null)?.let { getDrawableWithBackgroundColor(it, backColor.toArgb()) }
    val source = drawable?.let { getBitmapFromDrawable(it) }
    return source?.let { getRounded(it, backColor.toArgb(), Color.Black.toArgb()) }
}

fun getRounded(context: Context, uri: Uri, backColor: Color): Bitmap? {
    val `is` = context.contentResolver.openInputStream(uri)
    val drawable = Drawable.createFromStream(`is`, uri.toString())?.let { getDrawableWithBackgroundColor(it, backColor.toArgb()) }
    val source = drawable?.let { getBitmapFromDrawable(it) }
    //return source?.let { getRounded(it, backColor.toArgb(), Color.Black.toArgb()) }
    return source
}

fun Dp.toPx(context: Context): Float {
    return (this.value * context.resources.displayMetrics.density)
}

fun TextUnit.toPx(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.value, context.resources.displayMetrics)
}

fun getDeviceDensityString(context: Context): String {
    when (context.resources.displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> return "ldpi"
        DisplayMetrics.DENSITY_MEDIUM -> return "mdpi"
        DisplayMetrics.DENSITY_TV, DisplayMetrics.DENSITY_HIGH -> return "hdpi"
        DisplayMetrics.DENSITY_260, DisplayMetrics.DENSITY_280, DisplayMetrics.DENSITY_300, DisplayMetrics.DENSITY_XHIGH -> return "xhdpi"
        DisplayMetrics.DENSITY_340, DisplayMetrics.DENSITY_360, DisplayMetrics.DENSITY_400, DisplayMetrics.DENSITY_420, DisplayMetrics.DENSITY_440, DisplayMetrics.DENSITY_XXHIGH -> return "xxhdpi"
        DisplayMetrics.DENSITY_560, DisplayMetrics.DENSITY_XXXHIGH -> return "xxxhdpi"
    }
    return ""
}

@Composable
fun Version(context: Context, height: Dp) {
    //if (RELEASE) return
    val df = SimpleDateFormat("yyyyMMdd.HHmmss", Locale.getDefault())
    val bt = df.format(Date(stringResource(id = R.string.build_time).toLong()))
    val pi = context.packageManager.getPackageInfo(context.packageName, 0)
    val vs = "[${pi.versionName}(${if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) pi.longVersionCode else pi.versionCode})][RELEASE:$RELEASE][DEBUG:$DEBUG][$bt]"
    var version by remember { mutableStateOf(false) }
    var timer by remember { mutableStateOf(false) }
    LaunchedEffect(timer) {
        delay(1000)
        timer = false
    }
    //if (version) Log.v(__CLASSNAME__, "${getMethodName()}[timer:$timer][version:$version][$vs]")
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = /*withTap(context)*/ {
                            /** Called on Double Tap */
                            timer = true
                            version = false
                        },
                        onLongPress = withTap(context) {
                            /** Called on Long Press */
                            if (timer) version = !version
                            timer = false
                        },
                        onPress = withPress(context) {
                            /** Called when the gesture starts */
                            version = false
                        },
                        onTap = /*withTap(context)*/ {
                            /** Called on Tap */
                            version = false
                        },
                    )
                }
                //.clickable(
                //    onClick = withClick {}
                //)
                .align(Alignment.BottomEnd)
                //.fillMaxWidth()
                .height(height),
        ) {
            Text(
                text = "${stringResource(id = R.string.version)}:$vs",
                modifier = Modifier.align(Alignment.BottomEnd),
                fontSize = 6.sp,
                color = if (version) Color.Red else Color.Transparent,
                textAlign = TextAlign.Right,
            )
        }
    }
}

/**
 * How to play the platform CLICK sound in Jetpack Compose Button click
 * @see <a href="https://stackoverflow.com/questions/66080018/how-to-play-the-platform-click-sound-in-jetpack-compose-button-click">How to play the platform CLICK sound in Jetpack Compose Button click</a>
 */
@Composable
fun withCheck(onCheckedChange: (Boolean) -> Unit): (Boolean) -> Unit {
    val context = LocalContext.current
    return { checked ->
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).playSoundEffect(AudioManager.FX_KEY_CLICK)
        onCheckedChange(checked)
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

fun withTap(context: Context, onTap: () -> Unit): (Offset) -> Unit {
    return {
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).playSoundEffect(AudioManager.FX_KEY_CLICK)
        onTap()
    }
}

fun withPress(context: Context, onPress: () -> Unit): PressGestureScope.(Offset) -> Unit {
    return {
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).playSoundEffect(AudioManager.FX_KEY_CLICK)
        onPress()
    }
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
fun ColorChip(label: String, color: Color, text: Color = MaterialTheme.colorScheme.onPrimary, onColorClick: (color: Color, text: Color) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(8.dp)
            .clickable(onClick = withClick { onColorClick(color, text) }),
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
fun ColorChips(onColorClick: (color: Color, text: Color) -> Unit) {
    ColorChip(label = "primary", color = MaterialTheme.colorScheme.primary, text = MaterialTheme.colorScheme.onPrimary) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onPrimary", color = MaterialTheme.colorScheme.onPrimary, text = MaterialTheme.colorScheme.primary) { color, text -> onColorClick(color, text) }
    ColorChip(label = "primaryContainer", color = MaterialTheme.colorScheme.primaryContainer, text = MaterialTheme.colorScheme.onPrimaryContainer) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onPrimaryContainer", color = MaterialTheme.colorScheme.onPrimaryContainer, text = MaterialTheme.colorScheme.primaryContainer) { color, text -> onColorClick(color, text) }
    ColorChip(label = "inversePrimary", color = MaterialTheme.colorScheme.inversePrimary, text = MaterialTheme.colorScheme.primary) { color, text -> onColorClick(color, text) }
    ColorChip(label = "secondary", color = MaterialTheme.colorScheme.secondary, text = MaterialTheme.colorScheme.onSecondary) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onSecondary", color = MaterialTheme.colorScheme.onSecondary, text = MaterialTheme.colorScheme.secondary) { color, text -> onColorClick(color, text) }
    ColorChip(label = "secondaryContainer", color = MaterialTheme.colorScheme.secondaryContainer, text = MaterialTheme.colorScheme.onSecondaryContainer) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onSecondaryContainer", color = MaterialTheme.colorScheme.onSecondaryContainer, text = MaterialTheme.colorScheme.secondaryContainer) { color, text -> onColorClick(color, text) }
    ColorChip(label = "tertiary", color = MaterialTheme.colorScheme.tertiary, text = MaterialTheme.colorScheme.onTertiary) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onTertiary", color = MaterialTheme.colorScheme.onTertiary, text = MaterialTheme.colorScheme.tertiary) { color, text -> onColorClick(color, text) }
    ColorChip(label = "tertiaryContainer", color = MaterialTheme.colorScheme.tertiaryContainer, text = MaterialTheme.colorScheme.onTertiaryContainer) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onTertiaryContainer", color = MaterialTheme.colorScheme.onTertiaryContainer, text = MaterialTheme.colorScheme.tertiaryContainer) { color, text -> onColorClick(color, text) }
    ColorChip(label = "background", color = MaterialTheme.colorScheme.background, text = MaterialTheme.colorScheme.onBackground) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onBackground", color = MaterialTheme.colorScheme.onBackground, text = MaterialTheme.colorScheme.background) { color, text -> onColorClick(color, text) }
    ColorChip(label = "surface", color = MaterialTheme.colorScheme.surface, text = MaterialTheme.colorScheme.onSurface) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onSurface", color = MaterialTheme.colorScheme.onSurface, text = MaterialTheme.colorScheme.surface) { color, text -> onColorClick(color, text) }
    ColorChip(label = "surfaceVariant", color = MaterialTheme.colorScheme.surfaceVariant, text = MaterialTheme.colorScheme.onSurfaceVariant) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onSurfaceVariant", color = MaterialTheme.colorScheme.onSurfaceVariant, text = MaterialTheme.colorScheme.surfaceVariant) { color, text -> onColorClick(color, text) }
    ColorChip(label = "surfaceTint", color = MaterialTheme.colorScheme.surfaceTint) { color, text -> onColorClick(color, text) }
    ColorChip(label = "inverseSurface", color = MaterialTheme.colorScheme.inverseSurface, text = MaterialTheme.colorScheme.inverseOnSurface) { color, text -> onColorClick(color, text) }
    ColorChip(label = "inverseOnSurface", color = MaterialTheme.colorScheme.inverseOnSurface, text = MaterialTheme.colorScheme.inverseSurface) { color, text -> onColorClick(color, text) }
    ColorChip(label = "error", color = MaterialTheme.colorScheme.error, text = MaterialTheme.colorScheme.onError) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onError", color = MaterialTheme.colorScheme.onError, text = MaterialTheme.colorScheme.error) { color, text -> onColorClick(color, text) }
    ColorChip(label = "errorContainer", color = MaterialTheme.colorScheme.errorContainer, text = MaterialTheme.colorScheme.onErrorContainer) { color, text -> onColorClick(color, text) }
    ColorChip(label = "onErrorContainer", color = MaterialTheme.colorScheme.onErrorContainer, text = MaterialTheme.colorScheme.errorContainer) { color, text -> onColorClick(color, text) }
    ColorChip(label = "outline", color = MaterialTheme.colorScheme.outline, text = MaterialTheme.colorScheme.outlineVariant) { color, text -> onColorClick(color, text) }
    ColorChip(label = "outlineVariant", color = MaterialTheme.colorScheme.outlineVariant, text = MaterialTheme.colorScheme.outline) { color, text -> onColorClick(color, text) }
    ColorChip(label = "scrim", color = MaterialTheme.colorScheme.scrim) { color, text -> onColorClick(color, text) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompoChips(modifier: Modifier, padding: Dp, onColorClick: (color: Color, text: Color) -> Unit, content: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .padding(8.0.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(padding),
    ) {
        content()
        Spacer(modifier = Modifier.padding(2.0.dp))
        Text(
            text = "Column",
            modifier = Modifier
                .padding(horizontal = padding)
                .clickable(onClick = withClick {}),
        )
        Divider()
        var enabled by remember { mutableStateOf(true) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { enabled = !enabled },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Enabled")
            Switch(
                checked = enabled,
                onCheckedChange = withCheck { enabled = it },
                colors = SwitchDefaults.colors()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { enabled = !enabled },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Enabled")
            Switch(
                checked = enabled,
                onCheckedChange = withCheck { enabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { enabled = !enabled },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val checked by remember { mutableStateOf(true) }
            Text("Enabled")
            Switch(
                checked = enabled,
                onCheckedChange = withCheck { enabled = it },
                thumbContent = if (checked) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = withClick { },
            //colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            enabled = enabled,
        ) { Text(text = "Btn:primary", maxLines = 1, overflow = TextOverflow.Ellipsis) }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = withClick { },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inversePrimary),
            enabled = enabled,
        ) { Text(text = "Btn:inversePrimary", maxLines = 1, overflow = TextOverflow.Ellipsis) }
        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = withClick { },
            //colors = ButtonDefaults.filledTonalButtonColors(MaterialTheme.colorScheme.secondaryContainer),
            enabled = enabled,
        ) { Text(text = "FilledTonalButton:secondaryContainer", maxLines = 1, overflow = TextOverflow.Ellipsis) }
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = withClick { },
            //colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colorScheme.surface),
            enabled = enabled,
        ) { Text(text = "ElevatedButton:surface", maxLines = 1, overflow = TextOverflow.Ellipsis) }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = withClick { },
            //colors = ButtonDefaults.outlinedButtonColors(Color.Transparent),
            enabled = enabled,
        ) { Text(text = "OutlinedButton:Color.Transparent", maxLines = 1, overflow = TextOverflow.Ellipsis) }
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = withClick { },
            //colors = ButtonDefaults.textButtonColors(Color.Transparent),
            enabled = enabled,
        ) { Text(text = "TextButton:Color.Transparent", maxLines = 1, overflow = TextOverflow.Ellipsis) }
        IconButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = withClick { },
            //colors = IconButtonDefaults.iconButtonColors(Color.Transparent),
            enabled = enabled,
        ) {
            Row(modifier = Modifier.clickable(onClick = withClick {})) {
                Icon(Icons.Default.Add, contentDescription = "Add Icon")
                Text(text = "IconButton:Color.Transparent", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        var value1 by remember { mutableStateOf(TextFieldValue("TextField:surfaceVariant")) }
        TextField(
            label = { Text("label") },
            placeholder = { Text("placeholder") },
            value = value1, onValueChange = { value1 = it },
            singleLine = true,
            enabled = enabled,
            isError = false,
        )
        var value2 by remember { mutableStateOf(TextFieldValue("TextField:surfaceVariant:Error")) }
        TextField(
            label = { Text("label") },
            placeholder = { Text("placeholder") },
            value = value2, onValueChange = { value2 = it },
            singleLine = true,
            enabled = enabled,
            isError = true,
        )
        var value3 by remember { mutableStateOf(TextFieldValue("OutlinedTextField")) }
        OutlinedTextField(
            label = { Text("label") },
            placeholder = { Text("placeholder") },
            value = value3, onValueChange = { value3 = it },
            singleLine = true,
            enabled = enabled,
            isError = false,
        )
        var value4 by remember { mutableStateOf(TextFieldValue("OutlinedTextField:Error")) }
        OutlinedTextField(
            label = { Text("label") },
            placeholder = { Text("placeholder") },
            value = value4, onValueChange = { value4 = it },
            singleLine = true,
            enabled = enabled,
            isError = true,
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = withClick {}),
        ) {
            Text(
                text = "Card:surfaceVariant",
                modifier = Modifier.padding(12.0.dp),
            )
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = withClick {}),
        ) {
            Text(
                text = "ElevatedCard:surface",
                modifier = Modifier.padding(12.0.dp),
            )
        }
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = withClick {}),
        ) {
            Text(
                text = "OutlinedCard:surface",
                modifier = Modifier.padding(12.0.dp),
            )
        }
        AssistChip(
            onClick = withClick { },
            label = { Text("Assist chip") },
            leadingIcon = {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings",
                    Modifier.size(AssistChipDefaults.IconSize)
                )
            },
            enabled = enabled,
        )
        var selected by remember { mutableStateOf(false) }
        FilterChip(
            selected = selected,
            onClick = withClick { selected = !selected },
            label = { Text("Filter chip") },
            leadingIcon = {
                Icon(
                    Icons.Filled.Done,
                    contentDescription = "Done",
                    Modifier.size(FilterChipDefaults.IconSize)
                )
            },
            enabled = enabled,
        )
        InputChip(
            onClick = withClick { /*enabled = !enabled*/ },
            label = { Text("InputChip") },
            selected = enabled,
            avatar = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Localized description",
                    Modifier.size(InputChipDefaults.AvatarSize)
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Localized description",
                    Modifier.size(InputChipDefaults.AvatarSize)
                )
            },
            enabled = enabled,
        )
        SuggestionChip(
            onClick = withClick { },
            label = { Text("Suggestion chip") },
            enabled = enabled,
        )
        var sliderPosition by remember { mutableFloatStateOf(0f) }
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(),
            steps = 3,
            valueRange = 0f..50f,
            enabled = enabled,
        )
        Text(text = sliderPosition.toString())
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 3,
            valueRange = 0f..50f,
            enabled = enabled,
        )
        Text(text = sliderPosition.toString())
        var rangePosition by remember { mutableStateOf(0f..100f) }
        RangeSlider(
            value = rangePosition,
            steps = 5,
            onValueChange = { range -> rangePosition = range },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
            },
            enabled = enabled,
        )
        Text(text = sliderPosition.toString())
        Divider()
        var selectedTabIndex by remember { mutableStateOf(0) }
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            },
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = withClick {
                    selectedTabIndex = 0
                },
                enabled = enabled,
            ) {
                Icon(imageVector = Icons.Default.Face, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tab",
                    maxLines = 1,
                )
            }
            Tab(
                selected = selectedTabIndex == 1,
                onClick = withClick {
                    selectedTabIndex = 1
                },
                enabled = enabled,
            ) {
                Icon(imageVector = Icons.Default.Home, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tab",
                    maxLines = 1,
                )
            }
            Tab(
                selected = selectedTabIndex == 2,
                onClick = withClick {
                    selectedTabIndex = 2
                },
                enabled = enabled,
            ) {
                Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tab",
                    maxLines = 1,
                )
            }
            Tab(
                selected = selectedTabIndex == 3,
                onClick = withClick {
                    selectedTabIndex = 3
                },
                enabled = enabled,
            ) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tab",
                    maxLines = 1,
                )
            }
        }
        Divider()
        Text(
            text = "Colors",
            modifier = Modifier
                .padding(horizontal = padding)
                .clickable(onClick = withClick {}),
        )
        ColorChips { color, text -> onColorClick(color, text) }
    } //Column
}
