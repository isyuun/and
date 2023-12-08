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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import net.pettip.app.ComponentActivity
import net.pettip.gps.app.GPSApplication
import net.pettip.gpx.GPXParser
import net.pettip.gpx.Track
import java.util.Collections

/**
 * @Project     : PetTip-Android
 * @FileName    : NaverGpxComponentActivity
 * @Date        : 2023-12-08
 * @author      : isyuun
 * @description : net.pettip.map.app.naver
 * @see net.pettip.map.app.naver.NaverGpxComponentActivity
 */
open class NaverGpxComponentActivity : ComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    val application = GPSApplication.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()
    }

    protected open fun setContent() {
        setContent {
            GpxApp()
        }
    }
}

@Composable
fun GpxApp() {
    val application = GPSApplication.instance
    val context = LocalContext.current
    val tracks = Collections.synchronizedList(ArrayList<Track>())

    val file = application.last()
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
