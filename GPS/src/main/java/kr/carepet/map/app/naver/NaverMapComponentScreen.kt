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
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.widget.LocationButtonView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.carepet.DEBUG
import kr.carepet.data.pet.CurrentPetData
import kr.carepet.gps.R
import kr.carepet.gps.app.GPSApplication
import kr.carepet.gpx.GPX_CAMERA_ZOOM_ZERO
import kr.carepet.gpx.GPX_LATITUDE_ZERO
import kr.carepet.gpx.GPX_LONGITUDE_ZERO
import kr.carepet.gpx.TRACK_ZERO_NUM
import kr.carepet.gpx.Track
import kr.carepet.map._app.getRounded
import kr.carepet.map._app.toPx
import kr.carepet.map._app.toText
import kr.carepet.map.app.LoadingDialog
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

fun marker(position: LatLng, event: Track.EVENT): Marker? {
    val context = GPSApplication.instance.applicationContext
    val id = when (event) {
        Track.EVENT.nnn -> -1
        Track.EVENT.img -> -1
        Track.EVENT.pee -> R.drawable.marker_pee
        Track.EVENT.poo -> R.drawable.marker_poop
        Track.EVENT.mrk -> R.drawable.marker_marking
    }
    val back = when (event) {
        Track.EVENT.nnn -> Color.White
        Track.EVENT.img -> Color.White
        Track.EVENT.pee -> Color(0xFFEEBF00)
        Track.EVENT.poo -> Color(0xFF956A5C)
        Track.EVENT.mrk -> Color(0xFF4AB0F5)
    }
    return if (id > -1) marker(context, position, id, back) else null
}

fun mark(pet: CurrentPetData, event: Track.EVENT, position: LatLng, mapView: MapView): Marker? {
    GPSApplication.instance.mark(pet, event)
    val marker = marker(position, event)
    marker?.let {
        mapView.getMapAsync { naverMap ->
            it.map = naverMap
        }
    }
    return marker
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

@Composable
fun IconButton(
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
        IconButton(
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
fun ImageButton(
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
        IconButton(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NaverMapApp(source: FusedLocationSource) {
    Log.v(__CLASSNAME__, "${getMethodName()}[ST]")
    val application = GPSApplication.instance
    if (G.mapPetInfo.isEmpty()) {
        val pet = CurrentPetData("", TRACK_ZERO_NUM, "읎따", "", "", "", 0.0f)
        application.add(pet)
        application.add(pet)
        G.mapPetInfo = application.pets
    }
    val pets = G.mapPetInfo
    if (pets.size == 1) application.add(pets[0])
    val context = LocalContext.current
    val tracks = application.tracks
    val start = application.start
    Log.i(__CLASSNAME__, "${getMethodName()}[${start}][${tracks?.size}][${source.lastLocation}]$pets${application.pets}")

    val markers = remember { mutableListOf<Marker>() }
    val coords = remember { mutableListOf<LatLng>() }
    if (start) {
        markers.clear()
        coords.clear()
        tracks?.forEach { track ->
            val lat = track.latitude
            val lon = track.longitude
            val pos = LatLng(lat, lon)
            val mrk = marker(pos, track.event)
            mrk?.let { markers.add(it) }
            coords.add(pos)
        }
    }

    var position by remember { mutableStateOf(LatLng(GPX_LATITUDE_ZERO, GPX_LONGITUDE_ZERO)) }
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

    val buttonText = if (start) stringResource(id = R.string.walk_button_end) else stringResource(R.string.walk_button_start)
    val buttonColors = if (start) ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary) else ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)

    val scope = rememberCoroutineScope()

    val departure = stringResource(id = R.string.departure)
    val arrival = stringResource(id = R.string.arrival)


    var isLoading by remember { mutableStateOf(false) }
    Log.wtf(__CLASSNAME__, "${getMethodName()}[isLoading:$isLoading][${coords.size}]")
    if (coords.isNotEmpty()) {
        isLoading = false
    }

    var refresh by remember { mutableStateOf(false) }
    LaunchedEffect(refresh, position, coords) {
        scope.launch {
            Log.w(__CLASSNAME__, "::NaverMapApp@LaunchedEffect@${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
            mapView.getMapAsync { naverMap ->
                if (coords.isNotEmpty()) {
                    isLoading = false
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

    val activity = LocalContext.current as Activity

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var checkedAll by rememberSaveable { mutableStateOf(false) }
    checkedAll = (application.pets == pets)
    var checkedSel by rememberSaveable { mutableStateOf(false) }
    var event by remember { mutableStateOf(Track.EVENT.nnn) }
    var showPetsSheet by remember { mutableStateOf(false) }

    /** map */
    Box {
        AndroidView(
            factory = {
                mapView.apply {
                    getMapAsync { naverMap ->
                        Log.wtf(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                        naverMap.apply {
                            uiSettings.isCompassEnabled = source.isCompassEnabled
                            uiSettings.isZoomGesturesEnabled = !uiSettings.isZoomControlEnabled
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
    }

    /** top */
    WalkNaviInfo(application.start)

    /** bottom/right/left/walk */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp)
    ) {
        Log.i(__CLASSNAME__, "::NaverMapApp@Box${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
        /** bottom */
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                    showBottomSheet = false
                    application.resume()
                },
                sheetState = sheetState,
                dragHandle = {},
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(
                            start = 6.dp,
                            end = 6.dp
                        )
                        .padding(
                            horizontal = 20.dp,
                            vertical = 20.dp,
                        )
                        .padding(bottom = navigationBarHeight())
                ) {
                    if (!start) {
                        Text(
                            text = stringResource(id = R.string.walk_title_select),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        R.string.walk_check_select_all
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    checkedAll = !checkedAll
                                    if (checkedAll) application.add(pets) else application.remove()
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Checkbox(
                                checked = checkedAll,
                                onCheckedChange = {
                                    checkedAll = it
                                    if (checkedAll) application.add(pets) else application.remove()
                                },
                            )
                            Text(
                                text = stringResource(id = R.string.walk_check_select_all),
                                fontSize = 14.sp,
                                letterSpacing = (-0.7).sp
                            )
                        }
                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            items(pets) { pet ->
                                Log.i(__CLASSNAME__, "::NaverMapApp@ModalBottomSheet${getMethodName()}[${application.contains(pet)}][${pet}]")
                                Box {
                                    WalkPetButton(
                                        pet = pet,
                                        checked = application.contains(pet),
                                        onCheckedChange = { checked ->
                                            refresh = !refresh
                                            if (checked) application.add(pet) else application.remove(pet)
                                            Log.wtf(__CLASSNAME__, "::NaverMapApp@ModalBottomSheet${getMethodName()}[$checked][$refresh][${application.contains(pet)}][${application.pets}]")
                                        }
                                    )
                                }
                            }
                        }
                        R.string.walk_check_select
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { checkedSel = !checkedSel },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Checkbox(
                                checked = checkedSel,
                                onCheckedChange = { checkedSel = it },
                            )
                            Text(
                                text = stringResource(id = R.string.walk_check_select),
                                fontSize = 14.sp,
                                letterSpacing = (-0.7).sp
                            )
                        }
                        /** walk */
                        Button(
                            enabled = application.pets.isNotEmpty(),
                            onClick = {
                                Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                                application.start()
                                isLoading = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(14.dp),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.currentlocation),
                                    contentDescription = stringResource(id = R.string.track),
                                    tint = Color.White,
                                )
                                Text(
                                    text = stringResource(id = R.string.walk_button_start),
                                )
                            }
                        }
                    } else {
                        application.pause()
                        Text(
                            text = stringResource(id = R.string.walk_title_end),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        WalkInfoSheet()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            R.string.walk_button_finish
                            Button(
                                onClick = {
                                    Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${context.getString(R.string.walk_button_finish)}${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                    application.stop()
                                    if (start) {
                                        mapView.getMapAsync { naverMap ->
                                            val ender = marker(context = context, position = position, captionText = arrival)
                                            ender.map = naverMap
                                            naverMap.takeSnapshot(false) {
                                                application.preview = it
                                            }
                                        }
                                        G.toPost = true
                                        activity.finish()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1.0f),
                                contentPadding = PaddingValues(14.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                            ) {
                                Text(text = stringResource(id = R.string.walk_button_finish))
                            }
                            R.string.walk_button_resume
                            Button(
                                onClick = {
                                    Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${context.getString(R.string.walk_button_resume)}${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1.0f),
                                contentPadding = PaddingValues(14.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            ) {
                                Text(text = stringResource(id = R.string.walk_button_resume))
                            }
                        }
                    }
                }
            }
        }
        /** right */
        Log.w(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
        AnimatedVisibility(
            visible = true,
            modifier = Modifier.align(Alignment.BottomEnd),
        ) {
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
                IconButton(
                    onClick = {
                        Log.d(__CLASSNAME__, "::NaverMapApp@PEE${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                        if (!start) return@IconButton
                        event = Track.EVENT.pee
                        if (application.pets.size == 1) mark(application.pets[0], event, position, mapView)?.let { markers.add(it) }
                        else showPetsSheet = true
                    },
                    drawable = ImageVector.vectorResource(id = R.drawable.icon_pee),
                    description = stringResource(R.string.pee),
                    shape = CircleShape,
                    color = Color(0xFFEEBF00),
                    size = 40.dp
                )
                /** poo */
                IconButton(
                    onClick = {
                        Log.d(__CLASSNAME__, "::NaverMapApp@POO${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                        if (!start) return@IconButton
                        event = Track.EVENT.poo
                        if (application.pets.size == 1) mark(application.pets[0], event, position, mapView)?.let { markers.add(it) }
                        else showPetsSheet = true
                    },
                    drawable = ImageVector.vectorResource(id = R.drawable.icon_poop),
                    description = stringResource(R.string.poop),
                    shape = CircleShape,
                    color = Color(0xFF956A5C),
                    size = 40.dp
                )
                /** mrk */
                IconButton(
                    onClick = {
                        Log.d(__CLASSNAME__, "::NaverMapApp@MRK${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                        if (!start) return@IconButton
                        event = Track.EVENT.mrk
                        if (application.pets.size == 1) mark(application.pets[0], event, position, mapView)?.let { markers.add(it) }
                        else showPetsSheet = true
                    },
                    drawable = ImageVector.vectorResource(id = R.drawable.icon_marking),
                    description = stringResource(R.string.mark),
                    shape = CircleShape,
                    color = Color(0xFF4AB0F5),
                    size = 40.dp
                )
            }
        }
        /** left */
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 150.dp
                )
                .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /** note */
            IconButton(
                onClick = {
                    Log.d(__CLASSNAME__, "::NaverMapApp@NTE${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                    if (!start) return@IconButton
                },
                drawable = ImageVector.vectorResource(id = R.drawable.icon_list),
                description = stringResource(R.string.note),
                size = 40.dp
            )
            /** camera */
            IconButton(
                onClick = {
                    Log.d(__CLASSNAME__, "::NaverMapApp@CAM${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                    if (!start) return@IconButton
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        context.startActivity(intent)
                    } else {
                        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        val pm: PackageManager = context.packageManager
                        val info = pm.resolveActivity(i, 0)?.activityInfo
                        val intent = Intent()
                        intent.component = info?.let { ComponentName(it.packageName, it.name) }
                        intent.action = Intent.ACTION_MAIN
                        intent.addCategory(Intent.CATEGORY_LAUNCHER)
                        context.startActivity(intent)
                    }
                },
                drawable = ImageVector.vectorResource(id = R.drawable.icon_camera_map),
                description = stringResource(R.string.photo),
                size = 40.dp
            )
            val locationButton = mapView.findViewById<LocationButtonView>(com.naver.maps.map.R.id.navermap_location_button)
            locationButton?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = 16.5.dp.toPx(context).toInt()
                bottomMargin = 68.dp.toPx(context).toInt()
            }
        }
        /** walk */
        Button(
            onClick = {
                Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                if (pets.size == 1 && !start) {
                    application.start()
                    isLoading = true
                } else {
                    showBottomSheet = !showBottomSheet
                    mapView.getMapAsync { naverMap ->
                        if (tracks?.isNotEmpty() == true) {
                            val latLng = tracks.last().let { LatLng(it.latitude, it.longitude) }
                            latLng.let {
                                val position = CameraPosition(it, GPX_CAMERA_ZOOM_ZERO)
                                val update = CameraUpdate.toCameraPosition(position)
                                naverMap.moveCamera(update)
                            }
                            val distance = application._distance
                            if (distance != null && distance > 100) {
                                val latLng1 = tracks.first().let { LatLng(it.latitude, it.longitude) }
                                val latLng2 = tracks.last().let { LatLng(it.latitude, it.longitude) }
                                val bounds = latLng1.let { latLng2.let { it1 -> LatLngBounds(it, it1) } }
                                val padding = 48.dp.toPx(context).toInt()
                                bounds.let { naverMap.moveCamera(CameraUpdate.fitBounds(it, padding)) }
                            }
                        }
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(
                    horizontal = 28.dp,
                    vertical = 24.dp,
                )
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            contentPadding = PaddingValues(14.dp),
            colors = buttonColors
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.currentlocation),
                    contentDescription = stringResource(id = R.string.walk_button_start),
                    tint = Color.White,
                )
                Text(
                    text = buttonText,
                )
            }
        }
        val density = LocalDensity.current.density
        val metrics = context.resources.displayMetrics
        val width = metrics.widthPixels / density
        val height = metrics.heightPixels / density
        val right = (width - 64)
        val top = (height - height + 100)
        val zoom = mapView.findViewById<View>(com.naver.maps.map.R.id.navermap_zoom_control)
        zoom?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            rightMargin = right.dp.toPx(context).toInt()
        }
        val compass = mapView.findViewById<View>(com.naver.maps.map.R.id.navermap_compass)
        compass?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            rightMargin = right.dp.toPx(context).toInt()
            topMargin = top.dp.toPx(context).toInt()
        }
    }

    /** right */
    AnimatedVisibility(
        visible = showPetsSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xB0000000))
                .clickable(enabled = showPetsSheet) {
                    showPetsSheet = false
                    refresh = !refresh
                },
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 32.dp,
                        vertical = 100.dp,
                    )
                    .align(Alignment.BottomEnd),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                val item = when (event) {
                    Track.EVENT.nnn -> stringResource(id = R.string.nnn)
                    Track.EVENT.img -> stringResource(id = R.string.nnn)
                    Track.EVENT.pee -> stringResource(id = R.string.pee)
                    Track.EVENT.poo -> stringResource(id = R.string.poop)
                    Track.EVENT.mrk -> stringResource(id = R.string.mark)
                }
                val text = String.format(stringResource(id = R.string.walk_title_marking), item)
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(application.pets) { pet ->
                        ImageButton(
                            text = pet.petNm,
                            onClick = {
                                Log.d(__CLASSNAME__, "::NaverMapApp@PET${getMethodName()}[${start}][${tracks?.size}][${coords.size}][${markers.size}][${position.toText()}]")
                                if (!start) return@ImageButton
                                showPetsSheet = false
                                mark(pet, event, position, mapView)?.let { markers.add(it) }
                            },
                            drawable = ImageVector.vectorResource(id = R.drawable.walk_active),
                            description = stringResource(R.string.mark),
                            shape = CircleShape,
                            //size = 40.dp,
                        )
                    }
                }
            }
        }
    }

    LoadingDialog(
        loadingText = stringResource(id = R.string.walk_text_in_tracking),
        loadingState = isLoading
    )
    Log.v(__CLASSNAME__, "${getMethodName()}[ED]")
}

@Composable
fun WalkPetButton(pet: CurrentPetData, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val petNm: String = pet.petNm
    val petRprsImgAddr: String = pet.petRprsImgAddr

    var check by rememberSaveable { mutableStateOf(checked) }; check = checked
    val width = (LocalConfiguration.current.screenWidthDp.dp - 60.dp) / 3
    val height = width/* - 9.dp*/

    Log.w(__CLASSNAME__, "${getMethodName()}[$check][$checked]$pet, $checked, $onCheckedChange")

    Button(
        onClick = {
            check = !check
            onCheckedChange(check)
        },
        modifier = Modifier
            .size(
                width = width,
                height = height,
            )
            .padding(end = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        border = BorderStroke(0.1.dp, Color.LightGray),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(petRprsImgAddr)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    placeholder = painterResource(id = R.drawable.profile_default),
                    error = painterResource(id = R.drawable.profile_default),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = petNm,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Checkbox(
                    checked = check,
                    onCheckedChange = {
                        check = it
                        onCheckedChange(check)
                    },
                )
            }
        }
    }
}

@Composable
fun WalkInfoSheet() {
    val application = GPSApplication.instance
    val duration = remember { application.duration }
    val distance = remember { application.distance }
    Log.d(__CLASSNAME__, "${getMethodName()}[${System.currentTimeMillis()}][$duration][$distance]")
    Row(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(20.dp),
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        R.string.walk_title_duration
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
                fontWeight = FontWeight.Bold,
            )
        }
        R.string.walk_title_distance
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
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkNaviInfo(start: Boolean) {
    Log.wtf(__CLASSNAME__, "${getMethodName()}$start")
    val application = GPSApplication.instance
    var pet by remember { mutableStateOf(CurrentPetData("", "", "", "", "", "", 0.0f)) }
    if (application.pets.isNotEmpty()) pet = application.pets[0]
    var count by remember { mutableIntStateOf(0) }
    var duration by remember { mutableStateOf("00:00:00") }
    var distance by remember { mutableStateOf("0.00 km") }
    if (start) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000) // 1초마다 업데이트
                if (start) count++ else count = 0
                duration = if (DEBUG) "${application.duration}" else "${application.duration}"
                distance = if (DEBUG) "${application.distance}" else "${application.distance}"
            }
        }
    }
    R.string.walk_title_tip
    AnimatedVisibility(
        visible = !start,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                )
                .border(
                    width = 0.1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                )
                .padding(horizontal = 24.dp)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_bulb),
                    contentDescription = "",
                    tint = Color.Unspecified,
                )
                Text(
                    text = stringResource(id = R.string.walk_title_tip),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .basicMarquee(),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .basicMarquee(),
                text = stringResource(id = R.string.walk_title_tips),
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
            )
        }
    }
    R.string.walk_title_walking
    AnimatedVisibility(
        visible = start,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                )
                .border(
                    width = 0.1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                )
                .padding(horizontal = 24.dp)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleImageTopBar(size = 60, imageUri = pet.petRprsImgAddr)
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.walk_title_walking),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1.0f),
                        text = duration,
                        fontSize = 22.sp,
                        letterSpacing = (-0.0).sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        //style = TextStyle(background = Color.Yellow),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1.5f),
                        text = distance,
                        fontSize = 22.sp,
                        letterSpacing = (-0.0).sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        //style = TextStyle(background = Color.Yellow),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            //Text(
            //    text = "반려동물 변경",
            //    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //    fontSize = 14.sp,
            //    letterSpacing = (-0.6).sp,
            //    textDecoration = TextDecoration.Underline,
            //    color = design_skip,
            //    modifier = Modifier.clickable {
            //        viewModel.updateSheetChange("change")
            //        scope.launch { bottomSheetState.show() }
            //    }
            //)
        }
    }
}

@Composable
fun CircleImageTopBar(size: Int, imageUri: String?) {
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
