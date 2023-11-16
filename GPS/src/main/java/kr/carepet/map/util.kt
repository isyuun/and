/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.map

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
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.naver.maps.geometry.LatLng

/**
 * @Project     : carepet-android
 * @FileName    : __mapinterface.kt
 * @Date        : 2023. 09. 20.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
fun LatLng?.toText(): String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
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

fun Dp.toPx(context: Context): Float {
    return (this.value * context.resources.displayMetrics.density)
}

fun TextUnit.toPx(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.value, context.resources.displayMetrics)
}

@Composable
fun navigationBarHeight(): Dp {
    val context = LocalContext.current
    val density = context.resources.displayMetrics.density
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    val navigationBarHeight = if (resourceId > 0) {
        (context.resources.getDimensionPixelSize(resourceId) / density).dp
    } else {
        0.dp
    }
    return navigationBarHeight
}

fun getDeviceDensityString(context: Context): String? {
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