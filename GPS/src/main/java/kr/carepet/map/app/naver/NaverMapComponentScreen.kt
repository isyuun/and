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
 *  isyuun@care-pet.kr             2023. 10. 10.   description...
 */

package kr.carepet.map.app.naver

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
import kr.carepet.data.pet.CurrentPetData
import kr.carepet.gps.R
import kr.carepet.gps.app.GPSApplication
import kr.carepet.gpx.GPX_CAMERA_ZOOM_ZERO
import kr.carepet.gpx.GPX_LATITUDE_ZERO
import kr.carepet.gpx.GPX_LONGITUDE_ZERO
import kr.carepet.gpx.Track
import kr.carepet.map._app.getRounded
import kr.carepet.map._app.toPx
import kr.carepet.map._app.toText
import kr.carepet.singleton.G
import kr.carepet.util.Log
import kr.carepet.util.getMethodName


/**
 * @Project     : carepet-android
 * @FileName    : NaverMapComponentScreen.kt
 * @Date        : 2023. 10. 10.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
private val __CLASSNAME__ = Exception().stackTrace[0].fileName

fun marker(context: Context, position: LatLng?, captionText: String = ""): Marker {
    val marker = Marker()
    if (position != null) {
        marker.position = position
    }
    marker.width = (24 * 1.2f).dp.toPx(context).toInt()
    marker.height = (32 * 1.2f).dp.toPx(context).toInt()
    //marker.icon = MarkerIcons.BLUE
    marker.icon = OverlayImage.fromResource(R.drawable.marker_start)
    marker.captionText = captionText
    marker.captionColor = Color.White.toArgb()
    marker.captionHaloColor = Color.Gray.toArgb()
    marker.captionTextSize = (3.8 * 0.85f).sp.toPx(context)
    marker.captionOffset = (-32 * 0.9f).dp.toPx(context).toInt()
    return marker
}

fun marker(context: Context, position: LatLng?, id: Int, back: Color = Color.White): Marker {
    val marker = Marker()
    if (position != null) {
        marker.position = position
    }
    marker.width = (32 * 0.9f).dp.toPx(context).toInt()
    marker.height = (32 * 0.9f).dp.toPx(context).toInt()
    getRounded(context, id, back)?.let { marker.icon = OverlayImage.fromBitmap(it) }
    return marker
}

@Composable
fun pee(position: LatLng): Marker {
    val context = LocalContext.current
    val marker = marker(context, position, R.drawable.marker_pee, Color(0xFFEEBF00))
    return marker
}

@Composable
fun poo(position: LatLng): Marker {
    val context = LocalContext.current
    val marker = marker(context, position, R.drawable.marker_poop, Color(0xFF956A5C))
    return marker
}

@Composable
fun mrk(position: LatLng): Marker {
    val context = LocalContext.current
    val marker = marker(context, position, R.drawable.marker_marking, Color(0xFF4AB0F5))
    return marker
}

@Composable
fun img(position: LatLng): Marker {
    val context = LocalContext.current
    val marker = marker(context, position, R.drawable.marker_poop, Color(0xFF956A5C))
    return marker
}

@Composable
fun navigationBarHeight(): Dp {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    val navigationBarHeight = if (resourceId > 0) {
        (context.resources.getDimensionPixelSize(resourceId) / density).dp
    } else {
        0.dp
    }
    return navigationBarHeight
}

@Composable
fun rememberMapViewWithLifecycle(
    context: Context,
    mapOptions: NaverMapOptions = NaverMapOptions()
): MapView {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NaverMapApp(source: FusedLocationSource) {
    val pets = G.mapPetInfo
    val context = LocalContext.current
    val application = GPSApplication.getInstance()
    val tracks = application.service?.tracks
    val start = application.start
    Log.i(__CLASSNAME__, "${getMethodName()}[${start}][${tracks?.size}][${source.lastLocation}][$pets]")

    val markers = remember { mutableListOf<Marker>() }
    val coords = remember { mutableListOf<LatLng>() }
    if (start) {
        markers.clear()
        coords.clear()
        tracks?.forEach { track ->
            val lat = track.latitude
            val lon = track.longitude
            val pos = LatLng(lat, lon)
            when (track.event) {
                Track.EVENT.nnn -> "TODO()"
                Track.EVENT.img -> "TODO()"
                Track.EVENT.pee -> markers.add(pee(pos))
                Track.EVENT.poo -> markers.add(poo(pos))
                Track.EVENT.mrk -> markers.add(mrk(pos))
            }
            coords.add(pos)
        }
    }

    var position = remember { LatLng(GPX_LATITUDE_ZERO, GPX_LONGITUDE_ZERO) }
    source.lastLocation?.let { position = LatLng(it.latitude, it.longitude) }
    source.isCompassEnabled = true

    Log.w(__CLASSNAME__, "${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")

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

    val buttonText = if (start) "${stringResource(id = R.string.walk_button_end)}" else "${stringResource(R.string.walk_button_start)}"
    val buttonColor = if (start) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(Color.Blue)

    val coroutineScope = rememberCoroutineScope()

    val departure = stringResource(id = R.string.departure)
    val arrival = stringResource(id = R.string.arrival)

    LaunchedEffect(position, coords) {
        coroutineScope.launch {
            Log.w(__CLASSNAME__, "::NaverMapApp@LaunchedEffect@${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
            mapView.getMapAsync { naverMap ->
                if (coords.isNotEmpty()) {
                    val starter = marker(context = context, position = coords.first(), captionText = departure)
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
                        Log.wtf(__CLASSNAME__, "::NaverMapApp@LaunchedEffect@${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                    }
                }
                naverMap.locationOverlay.position = position
                markers.forEach {
                    it.map = naverMap
                }
            }
        }
    }
    val pee = pee(position)
    val poo = poo(position)
    val mrk = mrk(position)

    val activity = LocalContext.current as Activity

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var isCheck by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                dragHandle = {},
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 32.dp,
                        )
                ) {
                    if (start) {
                        Text(
                            text = stringResource(id = R.string.walk_title_end),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        WalkInfo()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Button(
                                onClick = {
                                    Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                    if (!start) {
                                        application.start()
                                    } else {
                                        application.stop()
                                    }
                                    if (start) {
                                        mapView.getMapAsync { naverMap ->
                                            val ender = marker(context = context, position = position, captionText = arrival)
                                            ender.map = naverMap
                                        }
                                        G.toPost = true
                                        activity.finish()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1.0f),
                                contentPadding = PaddingValues(14.dp),
                                colors = ButtonDefaults.buttonColors(Color.Red),
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.walk_button_finish),
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                            Button(
                                onClick = {
                                    Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1.0f),
                                contentPadding = PaddingValues(14.dp),
                                colors = ButtonDefaults.buttonColors(Color.Blue),
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.walk_button_resume),
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(id = R.string.walk_title_select),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.padding(top = 20.dp))
                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            items(pets) { pet ->
                                Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                                    WalkPet(pet = pet)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(top = 4.dp))

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable { isCheck = !isCheck },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isCheck,
                                    onCheckedChange = { isCheck = it },
                                )
                                Text(
                                    text = stringResource(id = R.string.walk_check_select),
                                    fontSize = 14.sp,
                                    letterSpacing = (-0.7).sp
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Button(
                                onClick = {
                                    Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                    if (!start) {
                                        application.start()
                                    } else {
                                        application.stop()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentPadding = PaddingValues(14.dp),
                                colors = ButtonDefaults.buttonColors(Color.Blue)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Row {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.currentlocation),
                                            contentDescription = stringResource(id = R.string.track),
                                            tint = Color.White,
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = stringResource(id = R.string.walk_button_start),
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(navigationBarHeight())
                            .fillMaxWidth()
                    )
                }
            }
        }
        Log.i(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
        AndroidView(
            factory = { context ->
                mapView.apply {
                    getMapAsync { naverMap ->
                        Log.wtf(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                        naverMap.apply {
                            locationSource = source
                            locationTrackingMode = LocationTrackingMode.Follow
                            cameraPosition = CameraPosition(cameraPosition.target, GPX_CAMERA_ZOOM_ZERO)
                            locationOverlay.isVisible = true
                            locationOverlay.circleRadius = 100
                            /**locationOverlay.anchor = PointF(0.0f, 0.0f)*/
                            /**locationOverlay.anchor = PointF(0.0f, 0.0f)*/
                            /**locationOverlay.anchor = PointF(0.0f, 0.0f)*/
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
                    vertical = 100.dp
                )
                .align(Alignment.BottomEnd),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /** pee */
            Row {
                //Text(stringResource(id = R.string.pee))
                IconButton(
                    onClick = {
                        Log.d(__CLASSNAME__, "::NaverMapApp@PEE${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                        if (!start) return@IconButton
                        mapView.getMapAsync { naverMap ->
                            pee.map = naverMap
                        }
                        markers.add(pee)
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
                        Log.d(__CLASSNAME__, "::NaverMapApp@POO${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                        if (!start) return@IconButton
                        mapView.getMapAsync { naverMap ->
                            poo.map = naverMap
                        }
                        markers.add(poo)
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
                        Log.d(__CLASSNAME__, "::NaverMapApp@MRK${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                        if (!start) return@IconButton
                        mapView.getMapAsync { naverMap ->
                            mrk.map = naverMap
                        }
                        markers.add(mrk)
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
                    vertical = 100.dp
                )
                .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /** note */
            Row {
                //Text(stringResource(id = R.string.pee))
                IconButton(
                    onClick = {
                        Log.d(__CLASSNAME__, "::NaverMapApp@NTE${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                        if (!start) return@IconButton
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
                        Log.d(__CLASSNAME__, "::NaverMapApp@CAM${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                        if (!start) return@IconButton
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
                leftMargin = 16.5.dp.toPx(context).toInt()
                bottomMargin = 42.dp.toPx(context).toInt()
            }
        }
        /** tracking */
        Button(
            onClick = {
                Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                showBottomSheet = !showBottomSheet
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 32.dp,
                    vertical = 24.dp,
                )
                .align(Alignment.BottomCenter),
            contentPadding = PaddingValues(14.dp),
            colors = buttonColor
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.currentlocation),
                        contentDescription = stringResource(id = R.string.track),
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
            rightMargin = right.dp.toPx(context).toInt()
        }
    }
}

@Composable
fun WalkPet(pet: CurrentPetData) {
    Log.wtf(__CLASSNAME__, "${getMethodName()}$pet")
    val petNm: String = pet.petNm
    val petRprsImgAddr: String = pet.petRprsImgAddr

    var check by remember { mutableStateOf(false) }
    val width = LocalConfiguration.current.screenWidthDp.dp - 60.dp

    Button(
        onClick = { check = !check },
        modifier = Modifier
            .size(
                width = width / 3,
                height = width / 3 - 9.dp
            ),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        border = BorderStroke(0.1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Spacer(modifier = Modifier.padding(top = 8.dp))
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
            ) {
                //if (petRprsImgAddr != null)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(petRprsImgAddr)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    placeholder = painterResource(id = R.drawable.profile_default),
                    error = painterResource(id = R.drawable.profile_default),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            }
            //Spacer(modifier = Modifier.padding(top = 8.dp))
            Row(
                modifier = Modifier
                    .clickable { check = !check },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = petNm,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Checkbox(
                    checked = check,
                    onCheckedChange = { check = it },
                )
            }
        }
    }
}

@Composable
fun WalkInfo() {
    val application = GPSApplication.getInstance()
    val duration = remember { application.service?.duration }
    val distance = remember { application.service?.distance }
    Row(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(20.dp),
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .padding(start = 24.dp, top = 16.dp, bottom = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.walk_title_duration),
                fontSize = 14.sp,
            )
            Text(
                text = "$duration",
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 24.dp, top = 16.dp, bottom = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.walk_title_distance),
                fontSize = 14.sp,
            )
            Text(
                text = "$distance",
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
