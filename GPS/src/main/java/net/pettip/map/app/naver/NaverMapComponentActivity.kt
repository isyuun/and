/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 25.   description...
 */

package net.pettip.map.app.naver

/**
 * @Project     : carepet-android
 * @FileName    : NaverMapComponentActivity.kt
 * @Date        : 2023. 09. 20.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.naver.maps.map.util.FusedLocationSource
import net.pettip.gps._app.toText
import net.pettip.gps.app.GPSApplication
import net.pettip.gps.app.GPSComponentActivity
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File

open class NaverMapComponentActivity : GPSComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private val application = GPSApplication.instance

    private val fusedLocationSource: FusedLocationSource by lazy {
        FusedLocationSource(this, NAVERMAP_PERMISSION_REQUEST_CODE)
    }

    protected open fun setContent() {
        setContent { MapApp() }
    }

    @Composable
    fun MapApp() {
        NaverMapApp()
    }

    @Composable
    private fun NaverMapApp() {
        Log.v(__CLASSNAME__, "${getMethodName()}[application.service:${application.service}]")
        application.service ?: return
        NaverMapApp(fusedLocationSource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w(__CLASSNAME__, "::NaverMapApp${getMethodName()}...")
        super.onCreate(savedInstanceState)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.d(__CLASSNAME__, "::NaverMapApp${getMethodName()}[$name][$service]")
        super.onServiceConnected(name, service)
        setContent()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.d(__CLASSNAME__, "::NaverMapApp${getMethodName()}[$name]")
        super.onServiceDisconnected(name)
        setContent()
    }

    override fun onResume() {
        Log.d(__CLASSNAME__, "::NaverMapApp${getMethodName()}...")
        super.onResume()
        setContent()
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.i(__CLASSNAME__, "::NaverMapApp${getMethodName()}[loading:...]${location?.toText()}, $location, $context, $intent")
        setContent()
    }

    override fun onCamera(file: File, uri: Uri) {
        Log.v(__CLASSNAME__, "::NaverMapApp${getMethodName()}[loading:...]${location?.toText()}, $location, $file")
        super.onCamera(file, uri)
        setContent()
    }
}