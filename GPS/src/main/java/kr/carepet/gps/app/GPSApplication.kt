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

package kr.carepet.gps.app

import android.Manifest
import android.content.ComponentName
import android.os.Build
import android.os.IBinder
import kr.carepet.gps._app.gpsapplication3
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : GPSApplication.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class GPSApplication : gpsapplication3() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    companion object {
        private var singleton: GPSApplication? = null

        @JvmStatic
        fun getInstance(): GPSApplication {
            return singleton ?: synchronized(this) {
                singleton ?: GPSApplication().also {
                    singleton = it
                }
            }
        }

        val permissions: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    }

    override fun onCreate() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onCreate()
        singleton = this
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceConnected(name, service)
        //start()    //test
    }
}