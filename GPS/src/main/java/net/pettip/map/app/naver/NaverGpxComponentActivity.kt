/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.8
 *
 */

package net.pettip.map.app.naver

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import net.pettip.app.ComponentActivity
import net.pettip.gps.app.GPSApplication
import net.pettip.gpx.GPXParser
import net.pettip.gpx.Track
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File
import java.util.Collections

/**
 * @Project     : PetTip-Android
 * @FileName    : NaverGpxComponentActivity
 * @Date        : 2023-12-08
 * @author      : isyuun
 * @description : net.pettip.map.app.naver
 * @see net.pettip.map.app.naver.NaverGpxComponentActivity
 */
open class NaverGpxComponentActivity : ComponentActivity(), ServiceConnection {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    val application = GPSApplication.instance

    protected open fun setContent() {
        setContent {
            GpxApp()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.v(__CLASSNAME__, "${getMethodName()}$name,$service")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.v(__CLASSNAME__, "${getMethodName()}$name")
    }
}

private val __CLASSNAME__ = Exception().stackTrace[0].fileName

@Preview
@Composable
fun GpxApp() {
    val application = GPSApplication.instance
    val file = application.recent()
    Log.wtf(__CLASSNAME__, "${getMethodName()}[$application][${application.service}][$file]")
    GpxApp(file = file)
}

@Composable
fun GpxApp(file: File?) {
    val application = GPSApplication.instance
    val context = LocalContext.current
    val tracks = Collections.synchronizedList(ArrayList<Track>())

    file?.let { GPXParser(tracks).read(it) }

    val mapView = rememberMapViewWithLifecycle(context)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = {
                mapView.apply {
                    getMapAsync { naverMap ->
                        naverMap.uiSettings.isZoomControlEnabled = false
                        naverMap.uiSettings.isLogoClickEnabled = false
                        naverMapView(context = context, naverMap = naverMap, tracks = tracks)
                        naverMap.takeSnapshot(false) {
                            application.preview = it
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
