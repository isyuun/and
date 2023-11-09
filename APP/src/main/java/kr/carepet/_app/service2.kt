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

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * @Project     : carepet-android
 * @FileName    : service2.kt
 * @Date        : 2023. 09. 11.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class service2 : service() {
    private val handler: Handler = Handler(Looper.getMainLooper())

    fun post(d: Long? = 0, r: Runnable) {
        handler.postDelayed(r, d!!)
    }

    fun remove(r: Runnable) {
        handler.removeCallbacks(r)
    }

    fun send(d: Long? = 0, m: Message) {
        handler.sendMessageDelayed(m, d!!)
    }

    fun handle(m: Message) {
        handler.handleMessage(m)
    }

    fun dispatch(m: Message) {
        handler.dispatchMessage(m)
    }
}