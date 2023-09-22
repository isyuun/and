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

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : gpsapplication3.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class gpsapplication3 : gpsapplication2() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private var _id = ""
    private var id: String
        get() = this._id
        set(id) {
            this._id = id
            foregroundOnlyLocationService?.id = this._id
        }

    override fun onCreate() {
        Log.i(__CLASSNAME__, "${getMethodName()}[$id]")
        super.onCreate()
        //this.id = ""
    }

    fun pee(id: String) {
        Log.d(__CLASSNAME__, "${getMethodName()}[$id]")
        this.id = id
        foregroundOnlyLocationService?.pee()
    }

    fun poo(id: String) {
        Log.d(__CLASSNAME__, "${getMethodName()}[$id]")
        this.id = id
        foregroundOnlyLocationService?.poo()
    }

    fun mark(id: String) {
        Log.d(__CLASSNAME__, "${getMethodName()}[$id]")
        this.id = id
        foregroundOnlyLocationService?.mark()
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
        super.onReceive(context, intent)
        //post { pee("") }    //test
        //post { poo("") }    //test
        //post { mark("") }    //test
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceConnected(name, service)
        foregroundOnlyLocationService?.onServiceConnected(name, service)
        start()    //test
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceDisconnected(name)
        foregroundOnlyLocationService?.onServiceDisconnected(name)
    }
}