/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.gps._app

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.WindowManager
import kr.carepet.app.AppCompatActivity
import kr.carepet.gps.app.ForegroundOnlyBroadcastReceiver2
import kr.carepet.gps.app.GPSApplication
import kr.carepet.gps.app.IForegroundOnlyBroadcastReceiver
import kr.carepet.util.Log
/**import kr.carepet.util.__CLASSNAME__*/
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : gpsappcompatactivity.kt
 * @Date        : 2023. 09. 04.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class gpsappcompatactivity : AppCompatActivity(), IForegroundOnlyBroadcastReceiver {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private val application: GPSApplication = GPSApplication.instance
    private lateinit var receiver: ForegroundOnlyBroadcastReceiver2

    internal fun location4Intent(intent: Intent): Location? {
        return application.location4Intent(intent)
    }

    protected var lastLocation: Location? = null
    override fun onReceive(context: Context, intent: Intent) {
        lastLocation = location4Intent(intent)
        Log.i(__CLASSNAME__, "${getMethodName()}${lastLocation?.toText()}, $lastLocation, $context, $intent")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onCreate(savedInstanceState)
        receiver = ForegroundOnlyBroadcastReceiver2(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStart() {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onStart()
        application.onStart()
    }

    override fun onResume() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$receiver")
        super.onResume()
        application.onResume()
        application.registerReceiver2(receiver)
    }

    override fun onPause() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$receiver")
        super.onPause()
        application.onPause()
        application.unregisterReceiver2(receiver)
    }

    override fun onStop() {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onStop()
        application.onStop()
    }
}