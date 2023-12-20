/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.20
 *
 */

package net.pettip._test.app.navi

/**import net.pettip.util.__CLASSNAME__*/

/**
 * @Project     : carepet-android
 * @FileName    : Application.kt
 * @Date        : 2023. 08. 22.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import net.pettip.map.app.MapApplication
import net.pettip.util.Log
import net.pettip.util.getMethodName

class Application : MapApplication() {
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