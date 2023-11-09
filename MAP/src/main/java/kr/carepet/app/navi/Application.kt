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
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.app.navi

/**import kr.carepet.util.__CLASSNAME__*/

/**
 * @Project     : carepet-android
 * @FileName    : Application.kt
 * @Date        : 2023. 08. 22.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kr.carepet.map.app.MapApplication
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

open class Application : MapApplication() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceConnected(name, service)
        //start()   //test
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
        super.onReceive(context, intent)
        //post { pee("") }    //test
        //post { poo("") }    //test
        //post { mark("") }    //test
    }
}