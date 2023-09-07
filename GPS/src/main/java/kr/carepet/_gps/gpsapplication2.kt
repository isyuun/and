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
 *  isyuun@care-pet.kr             2023. 9. 7.   description...
 */

package kr.carepet._gps

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : gpsapplication2.kt
 * @Date        : 2023. 09. 07.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class gpsapplication2 : gpsapplication() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceConnected(name, service)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceDisconnected(name)
    }

    override fun start() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.start()
    }

    override fun stop() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.stop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onSharedPreferenceChanged(sharedPreferences, key)
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onReceive(context, intent)
    }
}