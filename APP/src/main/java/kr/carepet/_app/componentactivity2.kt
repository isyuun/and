/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 8. 17.   description...
 */

package kr.carepet._app

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * @Project     : carepet-android
 * @FileName    : componentactivity2.kt
 * @Date        : 2023. 08. 17.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class componentactivity2 : componentactivity() {
    private val handler: Handler = Handler(Looper.getMainLooper())

    fun post(d: Long = 0, r: Runnable) {
        handler.postDelayed(r, d)
    }

    fun remove(r: Runnable) {
        handler.removeCallbacks(r)
    }

    fun send(d: Long = 0, m: Message) {
        handler.sendMessageDelayed(m, d)
    }

    fun handle(m: Message) {
        handler.handleMessage(m)
    }

    fun dispatch(m: Message) {
        handler.dispatchMessage(m)
    }
}