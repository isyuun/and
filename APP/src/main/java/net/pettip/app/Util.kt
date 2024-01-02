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
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.delay
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

fun root(context: Context): String {
    val ret = if (RELEASE) context.filesDir.path else context.getExternalFilesDirs("")[0].path
    return ret
}

fun pics(context: Context): String {
    val ret = "${root(context)}/.PIC"
    return ret
}

fun gpxs(context: Context): String {
    val ret = "${root(context)}/.GPX"
    return ret
}

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
    val vs = "[${pi.versionName}(${if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) pi.longVersionCode else pi.versionCode})][${if (RELEASE) "REL" else "DEB"}][$bt]"
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
