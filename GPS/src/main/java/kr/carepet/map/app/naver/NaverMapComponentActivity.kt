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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kr.carepet.gpx.Track.EVENT.img
import kr.carepet.gpx.Track.EVENT.mrk
import kr.carepet.gpx.Track.EVENT.nnn
import kr.carepet.gpx.Track.EVENT.pee
import kr.carepet.gpx.Track.EVENT.poo
import kr.carepet.map._app.getRounded
import kr.carepet.map._app.toPx
import kr.carepet.map._app.toText
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

open class NaverMapComponentActivity : GPSComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    //private lateinit var fusedLocationSource: FusedLocationSource
    private val fusedLocationSource: FusedLocationSource by lazy {
        FusedLocationSource(this, NAVERMAP_PERMISSION_REQUEST_CODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w(__CLASSNAME__, "::NaverMapApp${getMethodName()}...")
        super.onCreate(savedInstanceState)
        //fusedLocationSource = FusedLocationSource(this, NAVERMAP_PERMISSION_REQUEST_CODE)
    }

    override fun onResume() {
        Log.w(__CLASSNAME__, "::NaverMapApp${getMethodName()}...")
        super.onResume()
        setContent()
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.wtf(__CLASSNAME__, "::NaverMapApp${getMethodName()}${location?.toText()}, $location, $context, $intent")
        setContent()
    }

    protected open fun setContent() {
        setContent { NaverMapApp() }
    }

    @Composable
    fun NaverMapApp() {
        Log.d(__CLASSNAME__, "${getMethodName()}[$fusedLocationSource][${fusedLocationSource.lastLocation}]")
        NaverMapApp(fusedLocationSource)
    }
}
