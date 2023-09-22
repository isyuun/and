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
 *  isyuun@care-pet.kr             2023. 9. 21.   description...
 */

package kr.carepet.map._app.naver

/**
 * @Project     : carepet-android
 * @FileName    : NaverMapComponentActivity.kt
 * @Date        : 2023. 09. 20.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
import kotlinx.coroutines.launch
import kr.carepet.gps.R
import kr.carepet.gps._app.toText
import kr.carepet.gpx.GPX_CAMERA_ZOOM_ZERO
import kr.carepet.gpx.GPX_LATITUDE_ZERO
import kr.carepet.gpx.GPX_LONGITUDE_ZERO
import kr.carepet.map._app._mapcomponentactivity
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

const val PERMISSION_REQUEST_CODE = 100

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

open class NaverMapComponentActivity : _mapcomponentactivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    var paths = mutableListOf<LatLng>()
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
        //setContent { NaverMapApp() }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}${location?.toText()}, $location, $context, $intent")
        super.onReceive(context, intent)
        val lat = location?.latitude
        val lon = location?.longitude
        val lng = lat?.let { lon?.let { it1 -> LatLng(it, it1) } }
        lng?.let { paths.add(it) }
        post {
            setContent { NaverMapApp() }
        }
    }

    @Composable
    fun NaverMapApp() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${paths.size}]$paths, $this")
        NaverMapApp(locationSource, paths)
    }
}

private val __CLASSNAME__ = Exception().stackTrace[0].fileName

@Composable
fun NaverMapApp(locationSource: FusedLocationSource, paths: List<LatLng>) {
    val coords = remember { paths }

    val context = LocalContext.current

    var latLng = remember {
        LatLng(GPX_LATITUDE_ZERO, GPX_LONGITUDE_ZERO)
    }
    if (paths.isNotEmpty()) {
        val lat = paths.last().latitude
        val lon = paths.last().longitude
        latLng = LatLng(lat, lon)
    }

    val markers = remember {
        mutableStateListOf<LatLng>()
    }

    Log.i(__CLASSNAME__, "${getMethodName()}[${paths.size}][${coords.size}]${latLng.toText()}, $paths, $coords")

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
    }
    val mapView = rememberMapViewWithLifecycle(context, mapOptions)

    LaunchedEffect(latLng, coords) {
        coroutineScope.launch {
            Log.w(__CLASSNAME__, "::LaunchedEffect@${getMethodName()}[${paths.size}][${coords.size}]${latLng.toText()}, $coords")
            mapView.getMapAsync { naverMap ->
                if (coords.size > 1) {
                    val path = PathOverlay()
                    path.coords = coords
                    path.color = 0xFFFFDBDB.toInt()
                    path.outlineColor = 0xF0FF5000.toInt()
                    path.width = 50
                    path.globalZIndex = 10
                    path.outlineWidth = 3
                    path.map = naverMap
                    Log.wtf(__CLASSNAME__, "::LaunchedEffect@${getMethodName()}[${paths.size}][${coords.size}]${latLng.toText()}, $coords")
                }
                //naverMap.apply {
                //    locationOverlay.isVisible = true
                //    locationOverlay.position = latLng
                //    locationOverlay.bearing = 0f
                //    locationOverlay.icon = OverlayImage.fromResource(R.drawable.ic_location_overlay)
                //    locationOverlay.iconWidth = 100
                //    locationOverlay.iconHeight = 100
                //    //locationOverlay.anchor = PointF(0.0f, 0.0f)
                //    //locationOverlay.subIcon = OverlayImage.fromResource(R.drawable.ic_location_overlay_start)
                //    //locationOverlay.subIconWidth = 80
                //    //locationOverlay.subIconHeight = 80
                //    //locationOverlay.subAnchor = PointF(-10.0f, 20.0f)
                //    locationOverlay.circleRadius = 100
                //}
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
                        Log.d(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[${paths.size}][${coords.size}]$latLng , $coords")
                        naverMap.locationSource = locationSource
                        naverMap.locationTrackingMode = LocationTrackingMode.Face
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
        Button(
            onClick = {
                Log.d(__CLASSNAME__, "::NaverMapApp@Button${getMethodName()}[${paths.size}][${coords.size}]$latLng , $coords")
                val newMarker = Marker()
                val lat = 37.5 + markers.size * 0.01
                val lon = 127.0 + markers.size * 0.01
                newMarker.position = LatLng(lat, lon)
                mapView.getMapAsync { naverMap ->
                    newMarker.map = naverMap
                }
                markers.add(LatLng(lat, lon))
            },
            modifier = Modifier.padding(16.dp),
        ) {
            Text("마킹")
        }
    }
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
