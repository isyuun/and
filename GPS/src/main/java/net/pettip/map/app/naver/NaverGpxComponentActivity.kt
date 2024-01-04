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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import net.pettip.app.ComponentActivity
import net.pettip.gps._app.KEY_FOREGROUND_GPXFILE
import net.pettip.gps.app.GPSApplication
import net.pettip.gpx.GPXParser
import net.pettip.gpx.Track
import net.pettip.ui.theme.APPTheme
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
            APPTheme {
                Surface {
                    GpxApp()
                }
            }
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

@Composable
fun GpxApp() {
    val application = GPSApplication.instance
    val file = (LocalContext.current as? ComponentActivity)?.intent?.getStringExtra(KEY_FOREGROUND_GPXFILE)?.let { File(it) }
    Log.wtf(__CLASSNAME__, "${getMethodName()}[$application][${application.service}][$file]")
    GpxApp(file = file)
}

@Composable
fun GpxApp(file: File?) {
    val application = GPSApplication.instance
    val context = LocalContext.current
    val tracks = Collections.synchronizedList(ArrayList<Track>())

    file?.let { GPXParser(tracks).read(it) }

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val mapOptions = remember {
        NaverMapOptions()
            .logoClickEnabled(true)
            .mapType(NaverMap.MapType.Navi)
            .nightModeEnabled(isSystemInDarkTheme)
            .zoomControlEnabled(false)
    }
    val mapView = rememberMapViewWithLifecycle(context, mapOptions)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = {
                mapView.apply {
                    getMapAsync { naverMap ->
                        naverMapView(context = context, naverMap = naverMap, tracks = tracks, padding = 104.0.dp)
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
