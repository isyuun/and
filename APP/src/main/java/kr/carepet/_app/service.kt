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
 *  isyuun@care-pet.kr             2023. 9. 11.   description...
 */

package kr.carepet._app

import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * @Project     : carepet-android
 * @FileName    : service.kt
 * @Date        : 2023. 09. 11.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class service : android.app.Service() {
    private val localBinder = LocalBinder()

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: service = this@service
    }

    override fun onBind(intent: Intent): IBinder {
        return localBinder
    }
}