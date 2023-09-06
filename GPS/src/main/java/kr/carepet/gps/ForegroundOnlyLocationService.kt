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

package kr.carepet.gps

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kr.carepet._gps.foregroundonlylocationservice2
import kr.carepet.util.Log
import kr.carepet.util.__CLASSNAME__
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : ForegroundOnlyLocationService.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
class ForegroundOnlyLocationService : foregroundonlylocationservice2() {
    //private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private fun launcherIntent(): Intent? {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        Log.wtf(__CLASSNAME__, "${getMethodName()}$serviceRunningInForeground, $packageName, $launchIntent")
        return launchIntent
    }

    override fun launchActivityIntent(): Intent? {
        return launcherIntent()
    }

    override fun cancelIntent(): Intent {
        return Intent(this, this::class.java)
    }

    private val localBinder = LocalBinder()

    override fun startService() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$serviceRunningInForeground")
        startService(Intent(applicationContext, ForegroundOnlyLocationService::class.java))
    }

    override fun onBind(intent: Intent): IBinder {
        val ret = super.onBind(intent)
        Log.wtf(__CLASSNAME__, "${getMethodName()}:$ret, $localBinder")
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}:$intent")
        super.onRebind(intent)
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    open inner class LocalBinder : Binder() {
        internal val service: ForegroundOnlyLocationService
            get() = this@ForegroundOnlyLocationService
    }

    companion object {
        //private const val TAG = "ForegroundOnlyLocationService"

        private const val PACKAGE_NAME = "kr.carepet.app.navi"

        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"

        private const val NOTIFICATION_ID = 12345678

        private const val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_01"

        private const val INTERVAL_SECONDS = 5L
    }
}