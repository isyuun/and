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
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kr.carepet.gps.app.GPSComponentActivity
import kr.carepet.gpx.GPX_CAMERA_ZOOM_ZERO
import kr.carepet.gpx.GPX_LATITUDE_ZERO
import kr.carepet.gpx.GPX_LONGITUDE_ZERO
import kr.carepet.map._app.getRounded
import kr.carepet.map._app.toText
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

const val PERMISSION_REQUEST_CODE = 100

open class NaverMapComponentActivity : GPSComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private val application = GPSApplication.getInstance()

    private lateinit var source: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w(__CLASSNAME__, "::NaverMapApp${getMethodName()}...")
        super.onCreate(savedInstanceState)
        source = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
    }

    override fun onResume() {
        Log.w(__CLASSNAME__, "::NaverMapApp${getMethodName()}...")
        super.onResume()
        setContent { NaverMapApp() }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.wtf(__CLASSNAME__, "::NaverMapApp${getMethodName()}${location?.toText()}, $location, $context, $intent")
        setContent { NaverMapApp() }
    }

    @Composable
    fun NaverMapApp() {
        Log.d(__CLASSNAME__, "${getMethodName()}[$source][${source.lastLocation}]")
        NaverMapApp(source)
    }
}

private val __CLASSNAME__ = Exception().stackTrace[0].fileName

fun marker(context: Context, position: LatLng?, id: Int, back: Color = Color.White): Marker {
    val marker = Marker()
    if (position != null) {
        marker.position = position
    }
    marker.width = 96
    marker.height = 96
    marker.zIndex = 100
    getRounded(context, id, back)?.let { marker.icon = OverlayImage.fromBitmap(it) }
    return marker
}

fun starter(position: LatLng?): Marker {
    val marker = Marker()
    if (position != null) {
        marker.position = position
    }
    //marker.width = 96
    //marker.height = 96
    marker.zIndex = 1
    marker.icon = OverlayImage.fromResource(R.drawable.marker_start)
    return marker
}

@Composable
fun NaverMapApp(source: FusedLocationSource) {
    Log.i(__CLASSNAME__, "${getMethodName()}[$source][${source.lastLocation}]")
    val context = LocalContext.current

    val application = GPSApplication.getInstance()
    val tracks = application.service?.tracks

    var coords = remember { mutableListOf<LatLng>() }
    if (application.start) {
        val pathes = mutableListOf<LatLng>()
        tracks?.forEach { track ->
            val lat = track.latitude
            val lon = track.longitude
            pathes.add(LatLng(lat, lon))
        }
        coords = pathes
    }

    var position = remember { LatLng(GPX_LATITUDE_ZERO, GPX_LONGITUDE_ZERO) }
    if (tracks?.isNotEmpty() == true) {
        val lat = tracks.last().latitude
        val lon = tracks.last().longitude
        position = LatLng(lat, lon)
    }

    Log.w(__CLASSNAME__, "${getMethodName()}[${tracks?.size}][${coords.size}][$source?][${position.toText()}][$tracks][$coords]")

    val mapOptions = remember {
        NaverMapOptions()
            .camera(position.let { CameraPosition(it, GPX_CAMERA_ZOOM_ZERO) })
            .logoClickEnabled(true)
            .mapType(NaverMap.MapType.Basic)
            .locationButtonEnabled(true)
            .zoomControlEnabled(true)
            .compassEnabled(true)
            .zoomGesturesEnabled(true)
            .indoorEnabled(true)
    }
    val mapView = rememberMapViewWithLifecycle(context, mapOptions)

    val isStarted = remember { mutableStateOf(application.start) }
    val buttonText = if (isStarted.value) "${getString(context, R.string.track)} ${getString(context, R.string.stop)}" else "${getString(context, R.string.track)} ${getString(context, R.string.start)}"
    val buttonColor = if (isStarted.value) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(Color.Blue)

    val markers = remember { mutableListOf<Marker>() }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(position, coords) {
        coroutineScope.launch {
            Log.w(__CLASSNAME__, "::NaverMapApp@LaunchedEffect@${getMethodName()}[${tracks?.size}][${coords.size}][$source][${position}]$coords")
            mapView.getMapAsync { naverMap ->
                if (coords.isNotEmpty()) {
                    val starter = starter(coords.first())
                    starter.map = naverMap
                    if (coords.size > 1) {
                        val path = PathOverlay()
                        path.coords = coords
                        path.color = 0xA0FFDBDB.toInt()
                        path.outlineColor = 0xA0FF5000.toInt()
                        path.width = 18
                        path.globalZIndex = 10
                        path.outlineWidth = 3
                        path.map = naverMap
                        Log.wtf(__CLASSNAME__, "::NaverMapApp@LaunchedEffect@${getMethodName()}[${tracks?.size}][${coords.size}][$source][${position}]$coords")
                    }
                }
                naverMap.locationOverlay.position = position
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Log.i(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[${tracks?.size}][${coords.size}][$source][${source.lastLocation}][$position][$coords]")
        AndroidView(
            factory = { context ->
                mapView.apply {
                    getMapAsync { naverMap ->
                        Log.wtf(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[${tracks?.size}][${coords.size}][$source][${source.lastLocation}][$position][$coords]")
                        naverMap.apply {
                            locationSource = source
                            locationTrackingMode = LocationTrackingMode.Follow
                            cameraPosition = CameraPosition(cameraPosition.target, GPX_CAMERA_ZOOM_ZERO)
                            locationOverlay.isVisible = true
                            locationOverlay.circleRadius = 100
                            /**locationOverlay.anchor = PointF(0.0f, 0.0f)*/
                            locationOverlay.icon = OverlayImage.fromResource(R.drawable.currentlocation)
                            locationOverlay.iconWidth = 100
                            locationOverlay.iconHeight = 100
                            ///**locationOverlay.subAnchor = PointF(-0.0f, 0.0f)*/
                            //locationOverlay.subIcon = OverlayImage.fromResource(R.drawable.ic_location_overlay_start)
                            //locationOverlay.subIconWidth = 80
                            //locationOverlay.subIconHeight = 80
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
        /** right */
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
                        Log.d(__CLASSNAME__, "::NaverMapApp@PEE${getMethodName()}[${isStarted.value}][${markers.size}]")
                        if (!isStarted.value) return@IconButton
                        val marker = marker(context, position, R.drawable.marker_pee, Color(0xFFEEBF00))
                        mapView.getMapAsync { naverMap ->
                            marker.map = naverMap
                        }
                        markers.add(marker)
                        application.pee("")
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
                        modifier = Modifier.size(20.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.icon_pee),
                        contentDescription = stringResource(R.string.pee),
                        tint = Color(0xFFEEBF00)
                    )
                }
            }
            /** poo */
            Row {
                //Text(stringResource(id = R.string.pee))
                IconButton(
                    onClick = {
                        Log.d(__CLASSNAME__, "::NaverMapApp@POO${getMethodName()}[${isStarted.value}][${markers.size}]")
                        if (!isStarted.value) return@IconButton
                        val marker = marker(context, position, R.drawable.marker_poop, Color(0xFF956A5C))
                        mapView.getMapAsync { naverMap ->
                            marker.map = naverMap
                        }
                        markers.add(marker)
                        application.poo()
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
                        modifier = Modifier.size(20.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.icon_poop),
                        contentDescription = stringResource(R.string.poo),
                        tint = Color(0xFF956A5C)
                    )
                }
            }
            /** mrk */
            Row {
                //Text(stringResource(id = R.string.pee))
                IconButton(
                    onClick = {
                        Log.d(__CLASSNAME__, "::NaverMapApp@MRK${getMethodName()}[${isStarted.value}][${markers.size}]")
                        if (!isStarted.value) return@IconButton
                        val marker = marker(context, position, R.drawable.marker_marking, Color(0xFF4AB0F5))
                        mapView.getMapAsync { naverMap ->
                            marker.map = naverMap
                        }
                        markers.add(marker)
                        application.mrk()
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
                        modifier = Modifier.size(20.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.icon_marking),
                        contentDescription = stringResource(R.string.mark),
                        tint = Color(0xFF4AB0F5)
                    )
                }
            }
        }
        /** left */
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
                    vertical = 120.dp
                )
                .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /** note */
            Row {
                //Text(stringResource(id = R.string.pee))
                IconButton(
                    onClick = {
                        Log.d(__CLASSNAME__, "::NaverMapApp@NTE${getMethodName()}[${isStarted.value}][${markers.size}]")
                        if (!isStarted.value) return@IconButton
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
                        Log.d(__CLASSNAME__, "::NaverMapApp@CAM${getMethodName()}[${isStarted.value}][${markers.size}]")
                        if (!isStarted.value) return@IconButton
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        context.startActivity(intent)
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
            val locationButton = mapView.findViewById<LocationButtonView>(com.naver.maps.map.R.id.navermap_location_button)
            locationButton?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = 16.5.dp.toPx().toInt()
                bottomMargin = 63.dp.toPx().toInt()
            }
        }
        /** tracking */
        Button(
            onClick = {
                Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${getMethodName()}[${isStarted.value}][${markers.size}]")
                if (!isStarted.value) {
                    application.start()
                } else {
                    application.stop()
                }
                isStarted.value = application.start
                coords = mutableListOf()
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
            colors = buttonColor,
            border = BorderStroke(1.dp, Color.Gray),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.currentlocation),
                        contentDescription = getString(context, R.string.track),
                        tint = Color.White,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = buttonText,
                    )
                }
            }
        }
        val zoomControlButton = mapView.findViewById<ZoomControlView>(com.naver.maps.map.R.id.navermap_zoom_control)
        val density = LocalDensity.current.density
        val metrics = context.resources.displayMetrics
        val width = metrics.widthPixels / density
        val height = metrics.heightPixels / density
        val right = (width - 64)
        zoomControlButton?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            rightMargin = right.dp.toPx().toInt()
        }
        Log.d(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}$width, $height, $right")
    }
}

@Composable
private fun Dp.toPx(): Float {
    return (this.value * LocalDensity.current.density)
}

@Composable
fun rememberMapViewWithLifecycle(
    context: Context,
    mapOptions: NaverMapOptions = NaverMapOptions()
): MapView {
    //Log.i(__CLASSNAME__, "::NaverMapApp${getMethodName()}...$context, $mapOptions")

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
                //lifecycleObserver.value.onDestroy()
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
