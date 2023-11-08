/*
 *  Copyright 2011 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.map._app

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
import android.util.TypedValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.naver.maps.geometry.LatLng
import kr.carepet.gps.R

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

@Composable
fun IconButton2(
    text: String = "",
    onClick: () -> Unit,
    drawable: ImageVector,
    description: String,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colorScheme.onBackground,
    back: Color = MaterialTheme.colorScheme.background,
    border: Color = MaterialTheme.colorScheme.tertiary,
    size: Dp = 0.dp
) {
    val modifier = if (size > 0.dp) Modifier.size(size) else Modifier.fillMaxHeight()
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .background(Color.Transparent),
        )
        androidx.compose.material3.IconButton(
            onClick = onClick,
            modifier = modifier
                .background(
                    color = back,
                    shape = shape,
                )
                .border(
                    width = 0.1.dp,
                    color = border,
                    shape = shape,
                ),
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = drawable,
                contentDescription = description,
                tint = color
            )
        }
    }
}

@Composable
fun ImageButton2(
    text: String = "",
    onClick: () -> Unit,
    drawable: ImageVector,
    description: String,
    shape: Shape = RectangleShape,
    color: Color = LocalContentColor.current,
    back: Color = MaterialTheme.colorScheme.background,
    border: Color = MaterialTheme.colorScheme.tertiary,
    size: Dp = 0.dp
) {
    val modifier = if (size > 0.dp) Modifier.size(size) else Modifier.fillMaxHeight()
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .background(Color.Transparent),
        )
        androidx.compose.material3.IconButton(
            onClick = onClick,
            modifier = modifier
                .background(
                    color = back,
                    shape = shape,
                )
                .border(
                    width = 0.1.dp,
                    color = border,
                    shape = shape,
                ),
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = drawable,
                contentDescription = description,
                tint = color
            )
        }
    }
}

@Composable
fun CircleImageUrl(size: Int, imageUri: String?) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .border(shape = CircleShape, border = BorderStroke(3.dp, color = MaterialTheme.colorScheme.background))
            .shadow(elevation = 10.dp, shape = CircleShape, spotColor = Color.Gray)
            .clip(CircleShape)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = "",
            placeholder = painterResource(id = R.drawable.profile_default),
            error = painterResource(id = R.drawable.profile_default),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
