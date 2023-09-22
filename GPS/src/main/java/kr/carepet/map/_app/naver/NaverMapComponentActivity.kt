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
import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch
import kr.carepet.gps._app.toText
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

    override fun onReceive(context: Context, intent: Intent) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}${location?.toText()}, $location, $context, $intent")
        super.onReceive(context, intent)
        val lat = location?.latitude
        val lon = location?.longitude
        val lng = lat?.let { lon?.let { it1 -> LatLng(it, it1) } }
        lng?.let { paths.add(it) }
        //post {
        //}
        setContent { NaverMapApp() }
    }

    @Composable
    fun NaverMapApp() {
        NaverMapApp(this, paths)
    }
}

private val __CLASSNAME__ = Exception().stackTrace[0].fileName

@Composable
fun NaverMapApp(activity: Activity, paths: List<LatLng>) {
    val latLngZero = LatLng(GPX_LATITUDE_ZERO, GPX_LONGITUDE_ZERO)
    //Log.wtf(__CLASSNAME__, "${getMethodName()}[${paths.size}][${coords.size}]$latLngZero, $paths")
    //val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val mapView = rememberMapViewWithLifecycle()

    val coords = remember {
        //listOf(
        //    LatLng(37.123, 127.456),
        //    LatLng(37.456, 127.789)
        //    // 필요한 만큼 좌표를 추가합니다.
        //)
        paths
    }

    val lat = if (paths.isNotEmpty()) paths.last().latitude else GPX_LATITUDE_ZERO
    val lon = if (paths.isNotEmpty()) paths.last().longitude else GPX_LONGITUDE_ZERO

    val lntLng = remember {
        LatLng(lat, lon)
    }

    val markers = remember {
        mutableStateListOf<LatLng>()
    }

    val locationSource = remember {
        FusedLocationSource(activity, PERMISSION_REQUEST_CODE)
    }

    Log.i(__CLASSNAME__, "${getMethodName()}[${paths.size}][${coords.size}][${lntLng == paths.last()}][$lat, $lon]$lntLng, ${paths.last()}, $paths, $coords")

    fun RedrawMap() {
        val path = PathOverlay()
        if (coords.size > 2) path.coords = coords
        path.color = 0xFFFF0000.toInt()
        path.width = 10
        coroutineScope.launch {
            Log.wtf(__CLASSNAME__, "::RedrawMap@${getMethodName()}[${paths.size}][${coords.size}][${lntLng == paths.last()}][$lat, $lon]$lntLng, ${paths.last()}, $paths, $coords")
            mapView.getMapAsync { naverMap ->
                naverMap.cameraPosition = CameraPosition(lntLng, GPX_CAMERA_ZOOM_ZERO)
                path.map = naverMap
            }
        }
    }

    LaunchedEffect(lntLng, coords) {
        Log.w(__CLASSNAME__, "::LaunchedEffect@${getMethodName()}[${paths.size}][${coords.size}][${lntLng == paths.last()}][$lat, $lon]$lntLng, ${paths.last()}, $paths, $coords")
        RedrawMap()
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        //RedrawMap()
        val mapOptions = remember {
            NaverMapOptions()
                .logoClickEnabled(true)
                .camera(CameraPosition(latLngZero, GPX_CAMERA_ZOOM_ZERO))
                .mapType(NaverMap.MapType.Navi)
                .locationButtonEnabled(true)
                .zoomControlEnabled(true)
                .compassEnabled(true)
        }
        AndroidView(
            factory = { context ->
                Log.d(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[${paths.size}][${coords.size}][${lntLng == paths.last()}][$lat, $lon]$lntLng, ${paths.last()}, $coords")
                MapView(context, mapOptions).apply {
                    getMapAsync { naverMap ->
                        naverMap.locationSource = locationSource
                        naverMap.locationTrackingMode = LocationTrackingMode.Face
                        naverMap.cameraPosition = CameraPosition(lntLng, GPX_CAMERA_ZOOM_ZERO)
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
        Button(
            onClick = {
                Log.d(__CLASSNAME__, "::NaverMapApp@Button${getMethodName()}[${paths.size}][${coords.size}][${lntLng == paths.last()}][$lat, $lon]$lntLng, ${paths.last()}, $coords")
                val newMarker = Marker()
                val lat = 37.5 + markers.size * 0.01
                val lon = 127.0 + markers.size * 0.01
                newMarker.position = LatLng(lat, lon)
                mapView.getMapAsync {
                    newMarker.map = it
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
fun rememberMapViewWithLifecycle(): MapView {
    Log.i(__CLASSNAME__, "${getMethodName()}...")
    val context = LocalContext.current
    val mapView = remember {
        MapView(context)
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
