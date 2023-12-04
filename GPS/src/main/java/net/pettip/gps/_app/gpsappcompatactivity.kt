/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 20.   description...
 */

package net.pettip.gps._app

/**import net.pettip.util.__CLASSNAME__*/
import android.content.Context
import android.content.Intent
import net.pettip.app.AppCompatActivity
import net.pettip.gps.app.IForegroundOnlyBroadcastReceiver

/**
 * @Project     : carepet-android
 * @FileName    : gpsappcompatactivity.kt
 * @Date        : 2023. 09. 04.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class gpsappcompatactivity : AppCompatActivity(), IForegroundOnlyBroadcastReceiver {
    override fun onReceive(context: Context, intent: Intent) {
        //TODO("Not yet implemented")
    }
}