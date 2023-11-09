/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.gps._app

import android.content.ComponentName
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

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceConnected(name, service)
        this.service?.onServiceConnected(name, service)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceDisconnected(name)
        this.service?.onServiceDisconnected(name)
    }
}