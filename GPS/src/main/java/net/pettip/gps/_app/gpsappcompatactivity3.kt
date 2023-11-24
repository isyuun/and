/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.11.11
 *
 */

package net.pettip.gps._app

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

/**
 * @Project     : PetTip-Android
 * @FileName    : gpsappcompatactivity3
 * @Date        : 11/11/2023
 * @author      : isyuun
 * @description : net.pettip.gps._app
 * @see net.pettip.gps._app.gpsappcompatactivity3
 */
open class gpsappcompatactivity3 : gpsappcompatactivity2(), ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        //TODO("Not yet implemented")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        //TODO("Not yet implemented")
    }
}