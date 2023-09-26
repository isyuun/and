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
 *  isyuun@care-pet.kr             2023. 9. 25.   description...
 */

package kr.carepet.map.app.naver

/**
 * @Project     : carepet-android
 * @FileName    : NaverMapComponentActivity.kt
 * @Date        : 2023. 09. 20.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.getString
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.widget.LocationButtonView
import com.naver.maps.map.widget.ZoomControlView
import kotlinx.coroutines.launch
import kr.carepet.gps.R
import kr.carepet.gps._app.toText
import kr.carepet.gps.app.GPSApplication
import kr.carepet.gpx.GPX_CAMERA_ZOOM_ZERO
import kr.carepet.gpx.GPX_LATITUDE_ZERO
import kr.carepet.gpx.GPX_LONGITUDE_ZERO
import kr.carepet.map._app._mapcomponentactivity
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

const val PERMISSION_REQUEST_CODE = 100

open class NaverMapComponentActivity : _mapcomponentactivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    var paths = mutableListOf<LatLng>()
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paths.clear()
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
        locationSource.isCompassEnabled = true
    }

    override fun onResume() {
        super.onResume()
        if (GPSApplication.getInstance().service?.tracks?.isEmpty() == true) return
        GPSApplication.getInstance().service?.tracks?.forEach { track ->
            val loc = track.loc
            val lat = loc.latitude
            val lon = loc.longitude
            val lng = LatLng(lat, lon)
            paths.add(lng)
        }
        setContent { NaverMapApp() }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}${location?.toText()}, $location, $context, $intent")
        super.onReceive(context, intent)
        val lat = location?.latitude
        val lon = location?.longitude
        val lng = lat?.let { lon?.let { it1 -> LatLng(it, it1) } }
        lng?.let { paths.add(it) }
        setContent { NaverMapApp() }
    }

    @Composable
    fun NaverMapApp() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${paths.size}]$paths, $this")
        NaverMapApp(locationSource, paths)
    }
}


/**
 * Returns the `location` object as a human readable string.
 */
fun LatLng?.toText(): String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}

private val __CLASSNAME__ = Exception().stackTrace[0].fileName

private fun getBitmap(drawable: Drawable): Bitmap {
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

private fun getRounded(
    source: Bitmap,
    backgroundColor: Int,
    outlineWidth: Float,
    outlineColor: Int
): Bitmap {
    val width = source.width
    val height = source.height

    val padding = width / 5
    val newWidth = width - (padding * 2)
    val newHeight = height - (padding * 2)

    // Create a scaled bitmap with the calculated dimensions
    val scaledBitmap = Bitmap.createScaledBitmap(source, newWidth, newHeight, true)

    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    // Calculate the coordinates to center the scaled bitmap
    val left = (width - newWidth) / 2f
    val top = (height - newHeight) / 2f

    val paint = Paint()
    paint.isAntiAlias = true

    // Draw a solid background
    canvas.drawColor(backgroundColor)

    // Calculate the destination rect for drawing the rounded image with padding
    val rect = RectF(left, top, left + newWidth, top + newHeight)

    // Draw the rounded image inside the destination rect using scaledBitmap
    canvas.drawRoundRect(rect, width.toFloat(), height.toFloat(), paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(scaledBitmap, left, top, paint)

    // Draw the outline
    paint.xfermode = null
    paint.color = outlineColor
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = outlineWidth

    // Calculate the rect for the outer border
    val outerRect = RectF(1f, 0f, width.toFloat() - 2, height.toFloat() - 2)
    canvas.drawRoundRect(outerRect, width.toFloat(), height.toFloat(), paint)

    return output
}

fun getRounded(context: Context, id: Int): Bitmap {
    val source = getBitmap(context.resources.getDrawable(id, null))
    return getRounded(source, Color.White.toArgb(), 0.3f, Color.Black.toArgb())
}

@Composable
fun NaverMapApp(locationSource: FusedLocationSource, locations: List<LatLng>) {
    val context = LocalContext.current

    val coords = remember { locations }

    var latLng = remember {
        LatLng(GPX_LATITUDE_ZERO, GPX_LONGITUDE_ZERO)
    }
    if (locations.isNotEmpty()) {
        val lat = locations.last().latitude
        val lon = locations.last().longitude
        latLng = LatLng(lat, lon)
    }

    val markers = remember {
        mutableStateListOf<LatLng>()
    }

    Log.i(__CLASSNAME__, "${getMethodName()}[${locations.size}][${coords.size}]${latLng.toText()}, $locations, $coords")

    val coroutineScope = rememberCoroutineScope()

    val mapOptions = remember {
        NaverMapOptions()
            .camera(CameraPosition(latLng, GPX_CAMERA_ZOOM_ZERO))
            .logoClickEnabled(true)
            .mapType(NaverMap.MapType.Basic)
            .locationButtonEnabled(true)
            .zoomControlEnabled(true)
            .compassEnabled(true)
            .zoomGesturesEnabled(true)
            .indoorEnabled(true)
    }
    val mapView = rememberMapViewWithLifecycle(context, mapOptions)

    LaunchedEffect(latLng, coords) {
        coroutineScope.launch {
            Log.w(__CLASSNAME__, "::LaunchedEffect@${getMethodName()}[${locations.size}][${coords.size}]${latLng.toText()}, $coords")
            mapView.getMapAsync { naverMap ->
                if (coords.size > 1) {
                    val path = PathOverlay()
                    path.coords = coords
                    path.color = 0xA0FFDBDB.toInt()
                    path.outlineColor = 0xA0FF5000.toInt()
                    path.width = 24
                    path.globalZIndex = 10
                    path.outlineWidth = 3
                    path.map = naverMap
                    Log.wtf(__CLASSNAME__, "::LaunchedEffect@${getMethodName()}[${locations.size}][${coords.size}]${latLng.toText()}, $coords")
                }
                naverMap.apply {
                    locationOverlay.position = latLng
                    //locationOverlay.bearing = 0f
                }
                //naverMap.locationTrackingMode = LocationTrackingMode.Follow
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                mapView.apply {
                    getMapAsync { naverMap ->
                        Log.d(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[${locations.size}][${coords.size}]$latLng , $coords")
                        naverMap.locationSource = locationSource
                        naverMap.locationTrackingMode = LocationTrackingMode.Follow
                        naverMap.apply {
                            locationOverlay.isVisible = true
                            //locationOverlay.icon = OverlayImage.fromResource(R.drawable.currentlocation)
                            //locationOverlay.iconWidth = 100
                            //locationOverlay.iconHeight = 100
                            //locationOverlay.anchor = PointF(0.0f, 0.0f)
                            //locationOverlay.subIcon = OverlayImage.fromResource(R.drawable.ic_location_overlay_start)
                            //locationOverlay.subIconWidth = 80
                            //locationOverlay.subIconHeight = 80
                            //locationOverlay.subAnchor = PointF(-10.0f, 20.0f)
                            locationOverlay.circleRadius = 100
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
                    vertical = 120.dp
                )
                .align(Alignment.BottomEnd),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /** pee */
            Row {
                //Text(stringResource(id = R.string.pee))
                IconButton(
                    onClick = {
                        val marker = Marker()
                        marker.position = latLng
                        mapView.getMapAsync { naverMap ->
                            marker.map = naverMap
                        }
                        marker.width = 96
                        marker.height = 96
                        marker.zIndex = 100
                        //marker.icon = OverlayImage.fromResource(R.drawable.icon_pee)
                        marker.icon = OverlayImage.fromBitmap(getRounded(context, R.drawable.icon_pee))
                        markers.add(latLng)
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                        .border(
                            width = 0.1.dp,
                            color = Color.Gray,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.icon_pee),
                        contentDescription = stringResource(R.string.pee),
                    )
                }
            }
            /** poo */
            Row {
                //Text(stringResource(id = R.string.pee))
                IconButton(
                    onClick = {
                        val marker = Marker()
                        marker.position = latLng
                        mapView.getMapAsync { naverMap ->
                            marker.map = naverMap
                        }
                        marker.width = 96
                        marker.height = 96
                        marker.zIndex = 100
                        //marker.icon = OverlayImage.fromResource(R.drawable.icon_poop)
                        marker.icon = OverlayImage.fromBitmap(getRounded(context, R.drawable.icon_poop))
                        markers.add(latLng)
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                        .border(
                            width = 0.1.dp,
                            color = Color.Gray,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.icon_poop),
                        contentDescription = stringResource(R.string.poo),
                    )
                }
            }
            /** mrk */
            Row {
                //Text(stringResource(id = R.string.pee))
                IconButton(
                    onClick = {
                        val marker = Marker()
                        marker.position = latLng
                        mapView.getMapAsync { naverMap ->
                            marker.map = naverMap
                        }
                        marker.width = 96
                        marker.height = 96
                        marker.zIndex = 100
                        //marker.icon = OverlayImage.fromResource(R.drawable.icon_mark)
                        marker.icon = OverlayImage.fromBitmap(getRounded(context, R.drawable.icon_mark))
                        markers.add(latLng)
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                        .border(
                            width = 0.1.dp,
                            color = Color.Gray,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.icon_mark),
                        contentDescription = stringResource(R.string.mark),
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
                    vertical = 120.dp
                )
                .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /** list */
            Row {
                //Text(stringResource(id = R.string.pee))
                IconButton(
                    onClick = {
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.White,
                            //shape = CircleShape,
                        )
                        .border(
                            width = 0.1.dp,
                            color = Color.Gray,
                            //shape = CircleShape,
                        )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.icon_list),
                        contentDescription = stringResource(R.string.note),
                    )
                }
            }
            /** camera */
            Row {
                //Text(stringResource(id = R.string.pee))
                IconButton(
                    onClick = {
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.White,
                            //shape = CircleShape,
                        )
                        .border(
                            width = 0.1.dp,
                            color = Color.Gray,
                            //shape = CircleShape,
                        )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.icon_camera_map),
                        contentDescription = stringResource(R.string.photo),
                    )
                }
            }
            Spacer(modifier = Modifier.height(29.dp)) // 아래 마진 추가
            /** location */
            val locationButton = mapView.findViewById<LocationButtonView>(com.naver.maps.map.R.id.navermap_location_button)
            locationButton?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = 16.5.dp.toPx().toInt()
                bottomMargin = 63.dp.toPx().toInt()
            }
            val zoomControlButton = mapView.findViewById<ZoomControlView>(com.naver.maps.map.R.id.navermap_zoom_control)
            zoomControlButton?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                val density = LocalDensity.current.density
                val metrics = context.resources.displayMetrics
                val width = metrics.widthPixels / density
                val height = metrics.heightPixels / density
                val right = (width - 64)
                Log.wtf(__CLASSNAME__, "${getMethodName()}$width, $height, $right")
                rightMargin = right.dp.toPx().toInt()
            }
        }
        /** tracking */
        Button(
            onClick = {
                /*TODO:산책시작*/
            },
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 8.dp,
                disabledElevation = 0.dp
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 32.dp,
                    vertical = 40.dp,
                )
                .align(Alignment.BottomCenter),
            contentPadding = PaddingValues(14.dp),
            colors = ButtonDefaults.buttonColors(Color.Blue),
            border = BorderStroke(1.dp, Color.Gray),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.currentlocation),
                        //modifier = Modifier
                        //    .padding(
                        //        horizontal = 8.dp,
                        //    ),
                        contentDescription = getString(context, R.string.track),
                        tint = Color.White,
                    )
                    Text(
                        text = "${getString(context, R.string.track)} ${getString(context, R.string.start)}",
                    )
                }
            }
        }
        /** tracking */
    }
}

@Composable
private fun Dp.toPx(): Float {
    return (this.value * LocalDensity.current.density).toFloat()
}

@Composable
fun rememberMapViewWithLifecycle(context: Context, mapOptions: NaverMapOptions): MapView {
    Log.i(__CLASSNAME__, "${getMethodName()}...$mapOptions")

    val mapView = remember {
        MapView(context, mapOptions)
    }

    val lifecycleObserver = rememberUpdatedState(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                lifecycleObserver.value.onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                lifecycleObserver.value.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                lifecycleObserver.value.onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                lifecycleObserver.value.onStop()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                mapView.onDestroy()
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return mapView
}
