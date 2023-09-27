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
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.gps._app

/**import kr.carepet.util.__CLASSNAME__*/
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.WindowManager
import kr.carepet.app.ComponentActivity
import kr.carepet.gps.app.ForegroundOnlyBroadcastReceiver2
import kr.carepet.gps.app.GPSApplication
import kr.carepet.gps.app.IForegroundOnlyBroadcastReceiver
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : gpscomponentactivity.kt
 * @Date        : 2023. 09. 04.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class gpscomponentactivity : ComponentActivity(), IForegroundOnlyBroadcastReceiver {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private val application: GPSApplication = GPSApplication.getInstance()
    private lateinit var receiver: ForegroundOnlyBroadcastReceiver2

    internal fun location4Intent(intent: Intent): Location? {
        return application.location4Intent(intent)
    }

    protected var location: Location? = null
    override fun onReceive(context: Context, intent: Intent) {
        location = location4Intent(intent)
        Log.i(__CLASSNAME__, "${getMethodName()}${location?.toText()}, $location, $context, $intent")
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