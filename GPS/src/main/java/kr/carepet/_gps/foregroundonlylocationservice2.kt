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
 *  isyuun@care-pet.kr             2023. 9. 6.   description...
 */

package kr.carepet._gps

/**import kr.carepet.util.__CLASSNAME__*/
import android.app.Notification
import android.location.Location
import com.google.android.gms.location.LocationResult
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice2.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class foregroundonlylocationservice2 : foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private var notification: Notification? = null

    override fun onCreate() {
        super.onCreate()
        Log.wtf(__CLASSNAME__, "${getMethodName()}$fusedLocationProviderClient")
    }

    override fun generateNotification(location: Location?): Notification? {
        //Log.w(__CLASSNAME__, "${getMethodName()}$serviceRunningInForeground, ${this.notification != null}, $location, ${this.notification}")
        this.notification = super.generateNotification(location)
        return this.notification
    }

    override fun onLocationResult(locationResult: LocationResult) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}${location.toText()}, $location, $locationResult")
        super.onLocationResult(locationResult)
    }
}