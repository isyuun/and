/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
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
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.naver.maps.map.util.FusedLocationSource
import kr.carepet.gps._app.toText
import kr.carepet.gps.app.GPSComponentActivity
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

open class NaverMapComponentActivity : GPSComponentActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private val fusedLocationSource: FusedLocationSource by lazy {
        FusedLocationSource(this, NAVERMAP_PERMISSION_REQUEST_CODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w(__CLASSNAME__, "::NaverMapApp${getMethodName()}...")
        super.onCreate(savedInstanceState)
    }

    protected open fun setContent() {
        setContent { NaverMapApp() }
    }

    @Composable
    fun MapApp() {
        //Log.d(__CLASSNAME__, "${getMethodName()}[$fusedLocationSource][${fusedLocationSource.lastLocation}]")
        NaverMapApp()
    }

    @Composable
    private fun NaverMapApp() {
        //Log.d(__CLASSNAME__, "${getMethodName()}[$fusedLocationSource][${fusedLocationSource.lastLocation}]")
        NaverMapApp(fusedLocationSource)
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
}
