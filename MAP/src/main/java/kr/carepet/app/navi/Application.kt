/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
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