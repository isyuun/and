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

/**
 * @Project     : carepet-android
 * @FileName    : NaverMapComponentActivity.kt
 * @Date        : 2023. 09. 20.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
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
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.coroutines.launch
import kr.carepet.gps.app.GPSComponentActivity

open class NaverMapComponentActivity : GPSComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NaverMapApp()
        }
    }
}

@Composable
fun NaverMapApp() {
    val coroutineScope = rememberCoroutineScope()
    val mapView = rememberMapViewWithLifecycle()

    val mapOptions = remember {
        NaverMapOptions()
            .camera(CameraPosition(LatLng(37.5, 127.0), 10.0))
    }

    val pathCoordinates = remember {
        listOf(
            LatLng(37.123, 127.456),
            LatLng(37.456, 127.789)
            // 필요한 만큼 좌표를 추가합니다.
        )
    }

    val markers = remember {
        mutableStateListOf<LatLng>()
    }

    LaunchedEffect(true) {
        coroutineScope.launch {
            val naverMap = mapView.getMapAsync { naverMap ->
                naverMap.uiSettings.isZoomControlEnabled = true

                val path = PathOverlay()
                path.coords = pathCoordinates
                path.color = 0xFFFF0000.toInt()
                path.width = 10
                path.map = naverMap
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                val newMarker = Marker()
                val lat = 37.5 + markers.size * 0.01
                val lng = 127.0 + markers.size * 0.01
                newMarker.position = LatLng(lat, lng)
                mapView.getMapAsync {
                    newMarker.map = it
                }
                markers.add(LatLng(lat, lng))
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("마킹")
        }

        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    getMapAsync { naverMap ->
                        naverMap.uiSettings.isZoomControlEnabled = true
                        naverMap.cameraPosition = CameraPosition(LatLng(37.5, 127.0), 10.0)
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
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
