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

import android.content.Intent
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice2.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class foregroundonlylocationservice2 : foregroundonlylocationservice3() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private fun launcherIntent(): Intent? {
        return packageManager.getLaunchIntentForPackage(packageName)
    }

    override fun launchActivityIntent(): Intent? {
        val intent = launcherIntent()
        Log.i(__CLASSNAME__, "${getMethodName()}$EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, $intent, $this, ${this::class.java}")
        return intent
    }

    override fun cancelIntent(): Intent {
        val intent: Intent = Intent(this, this::class.java)
        Log.i(__CLASSNAME__, "${getMethodName()}$EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, $intent, $this, ${this::class.java}")
        return intent
    }

    override fun actionForegroundIntent(): Intent {
        val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, currentLocation)
        return intent
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(__CLASSNAME__, "${getMethodName()}$EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, $intent, $flags, $startId")     //Log.d(__CLASSNAME__, "onStartCommand()")
        return super.onStartCommand(intent, flags, startId)
    }
}