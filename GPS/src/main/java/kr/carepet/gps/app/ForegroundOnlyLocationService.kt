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

/**import kr.carepet.util.__CLASSNAME__*/
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kr.carepet.gps._app.foregroundonlylocationservice5
import kr.carepet.gps._app.foregroundonlylocationservice6
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : ForegroundOnlyLocationService.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
class ForegroundOnlyLocationService : foregroundonlylocationservice6() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: ForegroundOnlyLocationService
            get() = this@ForegroundOnlyLocationService
    }

    private val localBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        val ret = super.onBind(intent)
        Log.wtf(__CLASSNAME__, "${getMethodName()}[$serviceRunningInForeground], $ret, $localBinder")
        return localBinder
    }
}