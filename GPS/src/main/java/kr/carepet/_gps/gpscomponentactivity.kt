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
 *  isyuun@care-pet.kr             2023. 9. 5.   description...
 */

package kr.carepet._gps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kr.carepet.gps.ForegroundOnlyBroadcastReceiver2
import kr.carepet.gps.GPSApplication
import kr.carepet.gps.IForegroundOnlyBroadcastReceiver
import kr.carepet.util.Log
/**import kr.carepet.util.__CLASSNAME__*/
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : gpscomponentactivity.kt
 * @Date        : 2023. 09. 04.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class gpscomponentactivity : kr.carepet.app.ComponentActivity(), IForegroundOnlyBroadcastReceiver {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private val application: GPSApplication = GPSApplication.getInstance()
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver2

    override fun onReceive(context: Context, intent: Intent) {
        val location = GPSApplication.getInstance().location4Intent(intent)
        Log.w(__CLASSNAME__, "${getMethodName()}$location, $context, $intent")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w(__CLASSNAME__, "${getMethodName()}")
        super.onCreate(savedInstanceState)
        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver2(this)
    }

    override fun onStart() {
        Log.w(__CLASSNAME__, "${getMethodName()}")
        super.onStart()
        GPSApplication.getInstance().onStart()
    }

    override fun onResume() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$foregroundOnlyBroadcastReceiver")
        super.onResume()
        GPSApplication.getInstance().onResume()
        GPSApplication.getInstance().registerReceiver2(foregroundOnlyBroadcastReceiver)
    }

    override fun onPause() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$foregroundOnlyBroadcastReceiver")
        super.onPause()
        GPSApplication.getInstance().onPause()
        GPSApplication.getInstance().unregisterReceiver2(foregroundOnlyBroadcastReceiver)
    }

    override fun onStop() {
        Log.w(__CLASSNAME__, "${getMethodName()}")
        super.onStop()
        GPSApplication.getInstance().onStop()
    }
}