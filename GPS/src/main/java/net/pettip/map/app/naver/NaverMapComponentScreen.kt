/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 10. 10.   description...
 */

package net.pettip.map.app.naver

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationConstants
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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
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
import net.pettip.DEBUG
import net.pettip.app.Version
import net.pettip.app.getDeviceDensityString
import net.pettip.app.getRounded
import net.pettip.app.toPx
import net.pettip.app.withClick
import net.pettip.data.pet.CurrentPetData
import net.pettip.gps.R
import net.pettip.gps._app.GPS_CAMERA_ZOOM_ZERO
import net.pettip.gps._app.GPS_LATITUDE_ZERO
import net.pettip.gps._app.GPS_LONGITUDE_ZERO
import net.pettip.gps.app.GPSApplication
import net.pettip.gpx.TRACK_ZERO_URI
import net.pettip.gpx.Track
import net.pettip.gpx._distance
import net.pettip.map.app.LoadingDialog
import net.pettip.singleton.G
import net.pettip.util.Log
import net.pettip.util.getMethodName


/**
 * @Project     : carepet-android
 * @FileName    : NaverMapComponentScreen.kt
 * @Date        : 2023. 10. 10.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */

private val __CLASSNAME__ = Exception().stackTrace[0].fileName

@Composable
private fun CircleImageUrl(size: Int, imageUri: String?) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .border(shape = CircleShape, border = BorderStroke(3.0.dp, color = MaterialTheme.colorScheme.background))
            .shadow(elevation = 10.0.dp, shape = CircleShape, spotColor = Color.Gray)
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

@Composable
private fun IconButton2(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "",
    back: Color = MaterialTheme.colorScheme.background,
    shape: Shape = CircleShape,
    enabled: Boolean = true,
    color: Color = Color.White,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    val border: Color = MaterialTheme.colorScheme.outline
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.0.dp,
            alignment = Alignment.End,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (text.isNotEmpty())
            Text(
                text = text,
                color = color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.background(Color.Transparent),
            )
        IconButton(
            onClick = onClick,
            modifier = modifier
                .background(
                    shape = shape,
                    color = back,
                )
                .border(
                    shape = shape,
                    color = border,
                    width = 0.1.dp,
                ),
            enabled = enabled,
            colors = colors,
            interactionSource = interactionSource,
            content = content,
        )
    }
}

private fun LatLng?.toText(): String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}

private fun marker(context: Context, position: LatLng?, captionText: String = "", zIndex: Int = 1): Marker {
    val marker = Marker()
    if (position != null) {
        marker.position = position
    }
    marker.width = (24 * 1.5f).dp.toPx(context).toInt()
    marker.height = (32 * 1.5f).dp.toPx(context).toInt()
    //marker.icon = MarkerIcons.BLUE
    marker.icon = OverlayImage.fromResource(R.drawable.marker_start)
    marker.captionText = captionText
    marker.captionColor = Color.White.toArgb()
    marker.captionHaloColor = Color.Gray.toArgb()
    marker.captionTextSize = (3.8 * 0.85f).sp.toPx(context)
    marker.captionOffset = (-32 * 1.2f).dp.toPx(context).toInt()
    marker.zIndex = zIndex
    return marker
}

private fun starter(context: Context, position: LatLng?): Marker {
    return marker(context, position, context.resources.getString(R.string.departure), 1)
}

private fun ender(context: Context, position: LatLng?): Marker {
    return marker(context, position, context.resources.getString(R.string.arrival), 3)
}

private fun marker(context: Context, position: LatLng, uri: Uri, back: Color = Color.White, size: Int = 64): Marker? {
    if (uri == TRACK_ZERO_URI) return null
    val marker = Marker()
    marker.position = position
    marker.width = (size * 0.9f).dp.toPx(context).toInt()
    marker.height = (size * 0.9f).dp.toPx(context).toInt()
    getRounded(context, uri, back)?.let { marker.icon = OverlayImage.fromBitmap(it) }
    marker.zIndex = 2
    return marker
}

private fun marker(context: Context, position: LatLng, id: Int, back: Color = Color.White, size: Int = 32): Marker? {
    if (id == -1) return null
    val marker = Marker()
    marker.position = position
    marker.width = (size * 0.9f).dp.toPx(context).toInt()
    marker.height = (size * 0.9f).dp.toPx(context).toInt()
    getRounded(context, id, back)?.let { marker.icon = OverlayImage.fromBitmap(it) }
    marker.zIndex = 2
    return marker
}

private fun marker(position: LatLng, track: Track): Marker? {
    val context = GPSApplication.instance.applicationContext
    val id = when (track.event) {
        Track.EVENT.NNN -> -1
        Track.EVENT.IMG -> R.drawable.icon_camera_map
        Track.EVENT.PEE -> R.drawable.marker_pee
        Track.EVENT.POO -> R.drawable.marker_poop
        Track.EVENT.MRK -> R.drawable.marker_marking
    }
    val back = when (track.event) {
        Track.EVENT.NNN -> Color.White
        Track.EVENT.IMG -> Color.White
        Track.EVENT.PEE -> Color(0xFFEEBF00)
        Track.EVENT.POO -> Color(0xFF956A5C)
        Track.EVENT.MRK -> Color(0xFF4AB0F5)
    }
    val marker = when (track.event) {
        //Track.EVENT.IMG -> if (track.uri != TRACK_ZERO_URI) marker(context, position, track.uri, back) else marker(context, position, id, back)
        Track.EVENT.IMG -> marker(context, position, id, back, 48)
        else -> marker(context, position, id, back)
    }
    //Log.v(__CLASSNAME__, "${getMethodName()}::onCamera()[position:$position][track.event:${track.event}][track.uri:${track.uri}][marker:$marker]")
    return marker
}

private fun mark(track: Track, position: LatLng, mapView: MapView): Marker? {
    val marker = marker(position, track)
    marker?.let {
        mapView.getMapAsync { naverMap ->
            it.map = naverMap
        }
    }
    Log.v(__CLASSNAME__, "${getMethodName()}::onCamera()[position:$position][track.event:${track.event}][track.uri:${track.uri}][marker:$marker]")
    return marker
}

private fun mark(pet: CurrentPetData, event: Track.EVENT, position: LatLng, mapView: MapView): Marker? {
    val application = GPSApplication.instance
    application.mark(pet, event)
    val track = if (application.tracks?.isNotEmpty() == true) application.tracks?.last() else null
    val marker = track?.let { mark(track, position, mapView) }
    Log.i(__CLASSNAME__, "${getMethodName()}::onCamera()[position:$position][track.event:${track?.event}][track.uri:${track?.uri}][marker:$marker]")
    return marker
}

fun naverMapPath(context: Context, naverMap: NaverMap, tracks: MutableList<Track>, finished: Boolean) {
    val markers = mutableListOf<Marker>()
    val coords = mutableListOf<LatLng>()
    markers.clear()
    coords.clear()
    tracks.forEach { track ->
        marker(LatLng(track.latitude, track.longitude), track)?.let { markers.add(it) }
        coords.add(LatLng(track.latitude, track.longitude))
    }
    if (coords.isNotEmpty()) {
        starter(context = context, position = coords.first()).map = naverMap
        if (finished) {
            ender(context = context, position = coords.last()).map = naverMap
        }
        if (coords.size > 1) {
            val path = PathOverlay()
            path.coords = coords
            path.color = Color(0xA0FFDBDB).toArgb()
            path.outlineColor = Color(0xA0FF5000).toArgb()
            path.width = 18
            path.globalZIndex = 10
            path.outlineWidth = 3
            path.map = naverMap
        }
    }
    markers.forEach { it.map = naverMap }
}

fun naverMapPreview(context: Context, naverMap: NaverMap, tracks: MutableList<Track>, padding: Dp = 52.0.dp) {
    if (tracks.isNotEmpty()) {
        var lat1 = tracks.first().latitude
        var lon1 = tracks.first().longitude
        var lat2 = tracks.last().latitude
        var lon2 = tracks.last().longitude
        tracks.forEach {
            val lat = it.latitude
            val lon = it.longitude
            if (lat < lat1) lat1 = lat
            if (lon < lon1) lon1 = lon
            if (lat > lat2) lat2 = lat
            if (lon > lon2) lon2 = lon
        }
        val latLng1 = LatLng(lat1, lon1)
        val latLng2 = LatLng(lat2, lon2)
        val bounds = LatLngBounds(latLng1, latLng2)
        val update = CameraUpdate.fitBounds(bounds, padding.toPx(context).toInt())
        naverMap.moveCamera(update)
        if (tracks._distance() < 200.0f) {
            val center = bounds.center
            val position = CameraPosition(center, GPS_CAMERA_ZOOM_ZERO)
            val update2 = CameraUpdate.toCameraPosition(position)
            naverMap.moveCamera(update2)
        }
    }
}

fun naverMapView(context: Context, naverMap: NaverMap, tracks: MutableList<Track>, padding: Dp = 52.0.dp) {
    naverMapPath(context = context, naverMap = naverMap, tracks = tracks, finished = true)
    naverMapPreview(context = context, naverMap = naverMap, tracks = tracks, padding)
}

fun naverMapReturn(naverMap: NaverMap, camera: CameraPosition) {
    //val target = camera.target
    //val zoom = camera.zoom
    //val tilt = camera.tilt
    //val bearing = camera.bearing
    //val camera = CameraPosition(target, zoom, tilt, bearing)
    naverMap.cameraPosition = camera
    naverMap.locationTrackingMode = LocationTrackingMode.Follow
}

@Composable
fun rememberMapViewWithLifecycle(
    context: Context,
    mapOptions: NaverMapOptions = NaverMapOptions(),
    animate: Boolean = false
): MapView {
    val mapView = remember { MapView(context, mapOptions) }

    val lifecycleObserver = rememberUpdatedState(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                lifecycleObserver.value.onStart()
                if (animate) mapView.animate().alpha(1.0f).setDuration(AnimationConstants.DefaultDurationMillis.toLong()).start()
            }

            override fun onResume(owner: LifecycleOwner) {
                lifecycleObserver.value.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                lifecycleObserver.value.onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                lifecycleObserver.value.onStop()
                if (animate) mapView.animate().alpha(0.0f).setDuration(AnimationConstants.DefaultDurationMillis.toLong()).start()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                lifecycleObserver.value.onDestroy()
                //mapView.onDestroy()
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
private fun WalkPetRow(pet: CurrentPetData, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val petNm: String = pet.petNm
    val petRprsImgAddr: String = pet.petRprsImgAddr

    var check by rememberSaveable { mutableStateOf(checked) }; check = checked
    val width = (LocalConfiguration.current.screenWidthDp.dp - 60.0.dp) / 3
    val height = width/* - 9.0.dp*/

    Log.w(__CLASSNAME__, "${getMethodName()}[$check][$checked]$pet, $checked, $onCheckedChange")

    Button(
        onClick = withClick {
            check = !check
            onCheckedChange(check)
        },
        modifier = Modifier
            .size(
                width = width,
                height = height,
            )
            .padding(end = 12.0.dp),
        shape = RoundedCornerShape(12.0.dp),
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
                    .size(46.0.dp)
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
private fun WalkPetCol(pet: CurrentPetData, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val petNm: String = pet.petNm
    val petRprsImgAddr: String = pet.petRprsImgAddr

    var check by rememberSaveable { mutableStateOf(checked) }; check = checked

    Log.w(__CLASSNAME__, "${getMethodName()}[$check][$checked]$pet, $checked, $onCheckedChange")

    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clickable(onClick = withClick(context) {
                check = !check
                onCheckedChange(check)
            })
            .padding(start = 12.0.dp, end = 24.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(top = 16.0.dp, bottom = 16.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleImageUrl(size = 60, imageUri = petRprsImgAddr)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = petNm,
                fontSize = 16.sp,
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

@Composable
private fun WalkInfoSheet() {
    val application = GPSApplication.instance
    val duration = remember { application.duration }
    val distance = remember { application.distance }
    Log.d(__CLASSNAME__, "${getMethodName()}[${System.currentTimeMillis()}][$duration][$distance]")
    val shape = RoundedCornerShape(20.0.dp)
    Row(
        modifier = Modifier
            .padding(top = 16.0.dp, bottom = 16.0.dp)
            .clip(shape = shape)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = shape,
            )
            .border(
                width = 0.1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = shape
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        R.string.walk_title_duration
        Column(
            modifier = Modifier
                .padding(start = 24.0.dp, top = 24.0.dp, bottom = 24.0.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.walk_title_duration),
                fontSize = 14.sp,
            )
            Text(
                text = "$duration",
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 4.0.dp),
                fontWeight = FontWeight.Bold,
            )
        }
        Divider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(60.dp)
                .width(1.dp),
            color = MaterialTheme.colorScheme.outline
        )
        R.string.walk_title_distance
        Column(
            modifier = Modifier
                .padding(start = 24.0.dp, top = 24.0.dp, bottom = 24.0.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.walk_title_distance),
                fontSize = 14.sp,
            )
            Text(
                text = "$distance",
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 4.0.dp),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WalkInfoNavi(
    modifier: Modifier = Modifier,
    onGloballyPositioned: (LayoutCoordinates) -> Unit,
) {
    Box(modifier = modifier) {
        val application = GPSApplication.instance
        var start by remember { mutableStateOf(false) }
        //Log.v(__CLASSNAME__, ":;WalkInfoNavi()${getMethodName()}$start")
        start = application.start
        var pet by remember {
            mutableStateOf(
                CurrentPetData(
                    age = "",
                    ownrPetUnqNo = "",
                    petKindNm = "",
                    petNm = "",
                    petRprsImgAddr = "",
                    sexTypNm = "",
                    wghtVl = 0.0f,
                    petRelUnqNo = 0,
                    mngrType = "M"
                )
            )
        }
        if (application.pets.isNotEmpty()) pet = application.pets[0]
        var duration by remember { mutableStateOf("00:00:00") }
        var distance by remember { mutableStateOf("0.00 km") }
        /** 1초마다 업데이트*/
        LaunchedEffect(start) {
            Log.wtf(__CLASSNAME__, ":;WalkInfoNavi()${getMethodName()}$start")
            while (true) {
                //Log.w(__CLASSNAME__, ":;WalkInfoNavi()${getMethodName()}$start")
                delay(1000) // 1초마다 업데이트
                duration = if (application.pause) "${application.duration}" else "${application.__duration}"
                distance = "${application.distance}"
            }
        }
        Box(
            modifier = Modifier
                .onGloballyPositioned { onGloballyPositioned(it) }
        ) {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                R.string.walk_title_tip
                AnimatedVisibility(
                    visible = !start,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(bottomStart = 20.0.dp, bottomEnd = 20.0.dp),
                            )
                            .border(
                                width = 0.1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(bottomStart = 20.0.dp, bottomEnd = 20.0.dp),
                            )
                            .padding(horizontal = 24.0.dp)
                            .padding(vertical = 26.0.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(
                            space = 8.0.dp,
                            alignment = Alignment.CenterVertically,
                        ),
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
                                modifier = Modifier
                                    .padding(start = 4.0.dp)
                                    .basicMarquee(),
                                text = (if (DEBUG) "${stringResource(id = R.string.app_name)}:" else "") + stringResource(id = R.string.walk_title_tip),
                                fontSize = 16.sp,
                                letterSpacing = (-0.6).sp,
                                maxLines = 1,
                                //style = TextStyle(background = Color.Yellow),     //test
                            )
                        }
                        Text(
                            modifier = Modifier
                                .padding(start = 24.0.dp)
                                .basicMarquee(),
                            text = stringResource(id = R.string.walk_title_tips),
                            fontSize = 14.sp,
                            letterSpacing = (-0.7).sp,
                            maxLines = 1,
                            //style = TextStyle(background = Color.Yellow),     //test
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
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(bottomStart = 20.0.dp, bottomEnd = 20.0.dp),
                            )
                            .border(
                                width = 0.1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(bottomStart = 20.0.dp, bottomEnd = 20.0.dp),
                            )
                            .padding(horizontal = 24.0.dp)
                            .padding(vertical = 16.0.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 0.0.dp,
                            alignment = Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CircleImageUrl(size = 60, imageUri = pet.petRprsImgAddr)
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 12.0.dp)
                                .padding(vertical = 8.0.dp),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = (if (DEBUG) "${stringResource(id = R.string.app_name)}:" else "") + stringResource(id = R.string.walk_title_walking),
                                fontSize = 16.sp,
                                letterSpacing = (-0.6).sp,
                                fontWeight = FontWeight.Normal,
                                maxLines = 1,
                                //style = TextStyle(background = Color.Yellow),     //test
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(20.0.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .weight(1.0f),
                                    text = duration,
                                    fontSize = 22.sp,
                                    letterSpacing = (-0.0).sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Left,
                                    maxLines = 1,
                                    //style = TextStyle(background = Color.Yellow),     //test
                                )
                                Text(
                                    modifier = Modifier
                                        .weight(1.0f),
                                    text = distance,
                                    fontSize = 22.sp,
                                    letterSpacing = (-0.0).sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Left,
                                    maxLines = 1,
                                    //style = TextStyle(background = Color.Yellow),     //test
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NaverMapApp(source: FusedLocationSource) {
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val application = GPSApplication.instance
    if (!application.start) application.recent()?.let { recent ->
        application.restart()
    }
    val start = application.start
    val tracks = application.tracks
    Log.v(__CLASSNAME__, "${getMethodName()}[ST][start:$start][${tracks?.size}][loading:$loading][tracks?.isNotEmpty():${(tracks?.isNotEmpty())}]")

    if (G.mapPetInfo.isEmpty()) {   //test
        val pets = ArrayList<CurrentPetData>()
        val pet1 = CurrentPetData("", "P00000000000001", "", "1.읎", "", "", -0.1f, -1, "M")
        val pet2 = CurrentPetData("", "P00000000000002", "", "2.읎", "", "", -0.1f, -1, "M")
        pets.add(pet1)
        pets.add(pet2)
        G.mapPetInfo = pets
    }
    val pets = G.mapPetInfo
    val dels = ArrayList<CurrentPetData>()
    application.pets.forEach { pet -> if (!pets.contains(pet)) dels.add(pet) }
    application.remove(dels)
    if (pets.size == 1) application.add(pets[0])

    val location = application.lastLocation ?: source.lastLocation
    var position by rememberSaveable { mutableStateOf(if (location != null) LatLng(location.latitude, location.longitude) else LatLng(GPS_LATITUDE_ZERO, GPS_LONGITUDE_ZERO)) }
    var camera by rememberSaveable { mutableStateOf(CameraPosition(position, GPS_CAMERA_ZOOM_ZERO)) }

    Log.i(__CLASSNAME__, "${getMethodName()}[$start][${tracks?.size}][${source.lastLocation}]$pets${application.pets}")

    source.lastLocation?.let { position = LatLng(it.latitude, it.longitude) }
    source.isCompassEnabled = true

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val mapOptions = remember {
        NaverMapOptions()
            .logoClickEnabled(true)
            .mapType(NaverMap.MapType.Navi)
            .nightModeEnabled(isSystemInDarkTheme)
            .zoomControlEnabled(true)
            .compassEnabled(source.isCompassEnabled)
            .locationButtonEnabled(true)
            .zoomGesturesEnabled(true)
            .indoorEnabled(true)
            .camera(camera)
    }
    val mapView = rememberMapViewWithLifecycle(context, mapOptions)

    var refresh by remember { mutableStateOf(false) }
    val markers = remember { mutableListOf<Marker>() }
    if (start) {
        val size = markers.size
        markers.clear()
        tracks?.forEach { track ->
            marker(LatLng(track.latitude, track.longitude), track)?.let { markers.add(it) }
        }
        if (markers.size != size) refresh = !refresh
    }

    val scope = rememberCoroutineScope()
    Log.w(__CLASSNAME__, "${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
    LaunchedEffect(refresh, position) {
        scope.launch {
            mapView.getMapAsync { naverMap ->
                Log.v(__CLASSNAME__, "::NaverMapApp@LaunchedEffect@${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                if (start) {
                    tracks?.let { naverMapPath(context = context, naverMap = naverMap, tracks = it, finished = false) }
                }
                naverMap.locationOverlay.position = position
                if (start && tracks?.size == 1) {
                    naverMap.locationTrackingMode = LocationTrackingMode.Follow
                }
            }
        }
    }

    val buttonText = if (start) stringResource(id = R.string.walk_button_end) else stringResource(R.string.walk_button_start)
    val buttonColors = if (start) ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inversePrimary) else ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)

    val activity = LocalContext.current as Activity

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var checkedAll by rememberSaveable { mutableStateOf(false) }
    checkedAll = (application.pets == pets)
    var checkedSel by rememberSaveable { mutableStateOf(false) }
    var event by remember { mutableStateOf(Track.EVENT.NNN) }
    var showPetsSheet by remember { mutableStateOf(false) }

    /** map */
    Box {
        AndroidView(
            factory = {
                mapView.apply {
                    getMapAsync { naverMap ->
                        Log.wtf(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                        naverMap.apply {
                            Log.i(__CLASSNAME__, "::NaverMapApp@AndroidView${getMethodName()}[isSystemInDarkTheme:$isSystemInDarkTheme][isNightModeEnabled:$isNightModeEnabled]")
                            locationSource = source
                            locationTrackingMode = LocationTrackingMode.Follow
                            //cameraPosition = CameraPosition(cameraPosition.target, zoom)
                            cameraPosition = camera
                            locationOverlay.isVisible = true
                            locationOverlay.circleRadius = 100
                            /**locationOverlay.anchor = PointF(0.0f, 0.0f)*/
                            locationOverlay.icon = OverlayImage.fromResource(R.drawable.currentlocation)
                            locationOverlay.iconWidth = 100
                            locationOverlay.iconHeight = 100
                            ///**locationOverlay.subAnchor = PointF(-0.0f, 0.0f)*/
                            //locationOverlay.subIcon = OverlayImage.fromResource(R.drawable.marker_start)
                            //locationOverlay.subIconWidth = 80
                            //locationOverlay.subIconHeight = 80
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }

    val d = LocalDensity.current.density
    val p = context.resources.displayMetrics.densityDpi
    val i = getDeviceDensityString(context)

    val m = context.resources.displayMetrics

    val width = (m.widthPixels / d).dp
    val height = (m.heightPixels / d).dp

    val horizontal = 24.0.dp
    val vertical = 42.0.dp
    val space = 16.0.dp

    val l = (horizontal - 13.0.dp)
    var t: Dp
    val r = (horizontal - 13.0.dp)
    val b = 68.0.dp

    val zc = mapView.findViewById<View>(com.naver.maps.map.R.id.navermap_zoom_control)
    val co = mapView.findViewById<View>(com.naver.maps.map.R.id.navermap_compass)
    val lb = mapView.findViewById<LocationButtonView>(com.naver.maps.map.R.id.navermap_location_button)
    var lh by remember { mutableStateOf(0.0.dp) }

    /** TOP */
    WalkInfoNavi(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Log.wtf(__CLASSNAME__, "::NaverMapApp@::TOP${getMethodName()}[$it][${it.size}][${it.size.height}][${it.size.width}]")
        t = (it.size.height.div(d)).dp
        var zh = 89.0.dp
        if (zc.width > 0) zh = (zc.height / d).dp
        zc?.updateLayoutParams<FrameLayout.LayoutParams> {
            this.gravity = Gravity.TOP or Gravity.START
            this.topMargin = (t + space).toPx(context).toInt()
            this.marginStart = (l + 9.0.dp).toPx(context).toInt()
        }
        co?.updateLayoutParams<RelativeLayout.LayoutParams> {
            this.topMargin = (t + space + zh).toPx(context).toInt()
            this.marginStart = l.toPx(context).toInt()
        }
        lb?.updateLayoutParams<RelativeLayout.LayoutParams> {
            this.bottomMargin = b.toPx(context).toInt()
            this.marginStart = l.toPx(context).toInt()
        }
        lh = if (lb.height > 0) (lb.height / d).dp else 52.0.dp
    }

    /** LEFT/RIGHT/WALK */
    Box(
        modifier = Modifier
            .padding(
                horizontal = horizontal,
                vertical = vertical,
            )
            .fillMaxSize(),
    ) {
        /** LEFT */
        Log.w(__CLASSNAME__, "::NaverMapApp@::LEFT${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
        Column(
            modifier = Modifier
                .padding(bottom = (b + space + lh))
                .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(space)
        ) {
            ///** NOTE */
            //IconButton2(
            //    onClick = withClick {
            //        Log.d(__CLASSNAME__, "::NaverMapApp@NTE${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
            //        if (!start) return@withClick
            //    },
            //    back = Color.White,
            //    shape = RectangleShape,
            //) {
            //    Icon(
            //        imageVector = ImageVector.vectorResource(id = R.drawable.icon_list),
            //        contentDescription = stringResource(R.string.note),
            //        tint = Color.Black,
            //    )
            //}
            /** CAMERA */
            IconButton2(
                onClick = withClick {
                    Log.d(__CLASSNAME__, "::NaverMapApp@CAM${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                    if (!start) return@withClick
                    application.camera()
                },
                back = Color.White,
                shape = RectangleShape,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_camera_map),
                    contentDescription = stringResource(R.string.photo),
                    tint = Color.Black,
                )
            }
        }
        /** RIGHT */
        Log.w(__CLASSNAME__, "::NaverMapApp@::RIGHT${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
        Column(
            modifier = Modifier
                .padding(bottom = b)
                .align(Alignment.BottomEnd),
            verticalArrangement = Arrangement.spacedBy(space)
        ) {
            /** pee */
            IconButton2(
                onClick = withClick {
                    Log.d(__CLASSNAME__, "::NaverMapApp@PEE${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                    if (!start) return@withClick
                    event = Track.EVENT.PEE
                    if (application.pets.size == 1) mark(application.pets[0], event, position, mapView)?.let { markers.add(it) }
                    else showPetsSheet = true
                },
                back = Color.White,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_pee),
                    contentDescription = stringResource(R.string.pee),
                    tint = Color(0xFFEEBF00),
                    modifier = Modifier.size(20.0.dp),
                )
            }
            /** poo */
            IconButton2(
                onClick = withClick {
                    Log.d(__CLASSNAME__, "::NaverMapApp@POO${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                    if (!start) return@withClick
                    event = Track.EVENT.POO
                    if (application.pets.size == 1) mark(application.pets[0], event, position, mapView)?.let { markers.add(it) }
                    else showPetsSheet = true
                },
                back = Color.White,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_poop),
                    contentDescription = stringResource(R.string.poop),
                    tint = Color(0xFF956A5C),
                    modifier = Modifier.size(20.0.dp),
                )
            }
            /** mrk */
            IconButton2(
                onClick = withClick {
                    Log.d(__CLASSNAME__, "::NaverMapApp@MRK${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                    if (!start) return@withClick
                    event = Track.EVENT.MRK
                    if (application.pets.size == 1) mark(application.pets[0], event, position, mapView)?.let { markers.add(it) }
                    else showPetsSheet = true
                },
                back = Color.White,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_marking),
                    contentDescription = stringResource(R.string.mark),
                    tint = Color(0xFF4AB0F5),
                    modifier = Modifier.size(20.0.dp),
                )
            }
        }
        /** WALK */
        Button(
            onClick = withClick {
                Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${context.getString(R.string.walk_title_end)}${getMethodName()}[$start`][`${tracks?.size}][${markers.size}][${position.toText()}]")
                if (start)
                    mapView.getMapAsync { naverMap ->
                        camera = naverMap.cameraPosition
                        tracks?.let { naverMapPreview(context = context, naverMap = naverMap, tracks = it, padding = 104.0.dp) }
                    }
                if (pets.size == 1 && !start) {
                    application.start()
                    loading = true
                } else {
                    showBottomSheet = !showBottomSheet
                }
            },
            shape = RoundedCornerShape(12.0.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            contentPadding = PaddingValues(14.0.dp),
            colors = buttonColors
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.0.dp),
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
    }

    /** RIGHT */
    AnimatedVisibility(
        visible = showPetsSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.8f))
                .clickable(enabled = showPetsSheet) {
                    showPetsSheet = false
                    refresh = !refresh
                },
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = horizontal,
                        vertical = vertical + b,
                    )
                    .align(Alignment.BottomEnd),
                verticalArrangement = Arrangement.spacedBy(space),
                horizontalAlignment = Alignment.End
            ) {
                val item = when (event) {
                    Track.EVENT.NNN -> stringResource(id = R.string.nnn)
                    Track.EVENT.IMG -> stringResource(id = R.string.nnn)
                    Track.EVENT.PEE -> stringResource(id = R.string.pee)
                    Track.EVENT.POO -> stringResource(id = R.string.poop)
                    Track.EVENT.MRK -> stringResource(id = R.string.mark)
                }
                val text = String.format(stringResource(id = R.string.walk_title_marking), item)
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(space),
                    horizontalAlignment = Alignment.End
                ) {
                    items(application.pets) { pet ->
                        IconButton2(
                            onClick = withClick {
                                Log.d(__CLASSNAME__, "::NaverMapApp@PET${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                                if (!start) return@withClick
                                showPetsSheet = false
                                mark(pet, event, position, mapView)?.let { markers.add(it) }
                            },
                            text = pet.petNm,
                            back = Color.White,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.walk_active),
                                contentDescription = stringResource(R.string.mark),
                                tint = Color.Black,
                                modifier = Modifier.size(20.0.dp),
                            )
                        }
                    }
                }
            }
        }
    }

    /** BOTTOM */
    Log.i(__CLASSNAME__, "::NaverMapApp@Box::BOTTOM${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
    Log.d(__CLASSNAME__, "::NaverMapApp@Box::BOTTOM${getMethodName()}[${stringResource(id = R.string.departure)}][${stringResource(id = R.string.arrival)}]")
    val configuration = LocalConfiguration.current
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                application.resume()
                mapView.getMapAsync { naverMap ->
                    Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${context.getString(R.string.walk_button_resume)}${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                    naverMapReturn(naverMap, camera)
                }
            },
            sheetState = sheetState,
            dragHandle = {},
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(
                        horizontal = horizontal,
                        //vertical = vertical,
                    )
                    .padding(
                        top = 24.0.dp,
                        bottom = vertical,
                    )
                    .navigationBarsPadding(),
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(
                                maxHeight = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 360.0.dp else 160.0.dp
                            )
                            .padding(bottom = 10.0.dp)
                    ) {
                        items(pets) { pet ->
                            val checked = application.contains(pet)
                            Log.wtf(__CLASSNAME__, "::NaverMapApp@ModalBottomSheet${getMethodName()}[${checked}][${pet}]")
                            Divider()
                            Box {
                                WalkPetCol(
                                    pet = pet,
                                    checked = checked,
                                    onCheckedChange = { checked ->
                                        refresh = !refresh
                                        if (checked) application.add(pet) else application.remove(pet)
                                        Log.wtf(__CLASSNAME__, "::NaverMapApp@ModalBottomSheet${getMethodName()}[$checked][$refresh][${application.contains(pet)}][${application.pets}]")
                                    }
                                )
                            }
                        }
                    }
                    /** walk */
                    Button(
                        onClick = withClick {
                            Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                            val pest = ArrayList<CurrentPetData>()
                            application.pets.forEach { pet -> if (pets.contains(pet)) pest.add(pet) }
                            application.add(pest)
                            application.start()
                            loading = true
                        },
                        enabled = application.pets.isNotEmpty(),
                        shape = RoundedCornerShape(12.0.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(14.0.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.0.dp),
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
                    mapView.getMapAsync { naverMap ->
                        Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${context.getString(R.string.walk_title_end)}${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                    }
                    Text(
                        text = stringResource(id = R.string.walk_title_end),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    WalkInfoSheet()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        R.string.walk_button_finish
                        Button(
                            onClick = withClick {
                                Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${context.getString(R.string.walk_button_finish)}${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                                application.stop()
                                mapView.getMapAsync { naverMap ->
                                    ender(context = context, position = position).map = naverMap
                                    naverMap.takeSnapshot(false) {
                                        application.preview = it
                                    }
                                }
                                G.toPost = true
                                activity.finish()
                            },
                            shape = RoundedCornerShape(12.0.dp),
                            modifier = Modifier.weight(1.0f),
                            contentPadding = PaddingValues(14.0.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inversePrimary),
                        ) {
                            Text(text = stringResource(id = R.string.walk_button_finish))
                        }
                        R.string.walk_button_resume
                        Button(
                            onClick = withClick {
                                Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${context.getString(R.string.walk_button_resume)}${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                                application.resume()
                                mapView.getMapAsync { naverMap ->
                                    Log.wtf(__CLASSNAME__, "::NaverMapApp@TRK${context.getString(R.string.walk_button_resume)}${getMethodName()}[$start][${tracks?.size}][${markers.size}][${position.toText()}]")
                                    naverMapReturn(naverMap, camera)
                                }
                            },
                            shape = RoundedCornerShape(12.0.dp),
                            modifier = Modifier.weight(1.0f),
                            contentPadding = PaddingValues(14.0.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        ) {
                            Text(text = stringResource(id = R.string.walk_button_resume))
                        }
                    }
                }
            }
        }
    }

    Log.i(__CLASSNAME__, "::NaverMapApp@${getMethodName()}[start:$start][${tracks?.size}][loading:$loading][tracks?.isNotEmpty():${(tracks?.isNotEmpty())}]")
    if (tracks?.isNotEmpty() == true) loading = false
    Log.w(__CLASSNAME__, "::NaverMapApp@${getMethodName()}[start:$start][${tracks?.size}][loading:$loading][tracks?.isNotEmpty():${(tracks?.isNotEmpty())}]")
    LoadingDialog(
        loadingText = stringResource(id = R.string.walk_text_in_tracking),
        loadingState = loading
    )
    Log.v(__CLASSNAME__, "${getMethodName()}[ED][start:$start][${tracks?.size}][loading:$loading][tracks?.isNotEmpty():${(tracks?.isNotEmpty())}]")
    /** VERSION */
    Version(context, vertical)
}

@Composable
fun ShowDialogRestart(
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = { Text(stringResource(id = R.string.walk_text_in_running)) },
    text: @Composable (() -> Unit)? = { Text(stringResource(id = R.string.walk_text_in_restore)) },
    onDismissRequest: () -> Unit,
    onDismissButton: () -> Unit,
    onConfirmButton: () -> Unit,
) {
    Box {
        val application = GPSApplication.instance
        var showDialog by remember { mutableStateOf(false) }
        if (application.start) application.openMap()
        else application.recent()?.let { recent -> showDialog = recent.exists() }
        Log.wtf(__CLASSNAME__, "${getMethodName()}[$showDialog][${application.start}][${application.recent()?.exists()}][${application.recent()}]")
        if (showDialog) {
            AlertDialog(
                icon = icon,
                title = title,
                text = text,
                onDismissRequest = withClick {
                    showDialog = false
                    application.reset()
                    onDismissRequest()
                },
                confirmButton = {
                    Button(
                        onClick = withClick {
                            showDialog = false
                            application.restart()
                            onConfirmButton()
                        }
                    ) {
                        Text(stringResource(id = android.R.string.ok))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = withClick {
                            showDialog = false
                            application.reset()
                            onDismissButton()
                        }
                    ) {
                        Text(stringResource(id = android.R.string.cancel))
                    }
                }
            )
        }//showDialog
    }
}
