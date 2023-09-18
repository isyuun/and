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
 *  isyuun@care-pet.kr             2023. 9. 14.   description...
 */

package kr.carepet._gps

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.google.android.gms.location.LocationResult
import kr.carepet.gpx.Location
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice3.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class foregroundonlylocationservice3 : foregroundonlylocationservice2(), ServiceConnection {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate() {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
        super.onCreate()
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
    }

    override fun onLocationResult(locationResult: LocationResult) {
        super.onLocationResult(locationResult)
    }

    fun pee(id: String = "") {
        val location = currentLocation?.let { Location(it, id, 1, 0) }
        location?.let { add(it) }
    }

    fun poo(id: String = "") {
        val location = currentLocation?.let { Location(it, id, 0, 1) }
        location?.let { add(it) }
    }
}