/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.27
 *
 */

package net.pettip.app.navi

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import net.pettip.gps.app.GPSApplication
import net.pettip.util.Log
import net.pettip.util.getMethodName

/**
 * @Project     : PetTip-Android
 * @FileName    : MainActivity
 * @Date        : 2023-12-27
 * @author      : isyuun
 * @description : net.pettip.app.navi
 * @see net.pettip.app.navi.MainActivity
 */
class MainActivity : net.pettip._test.app.navi.MainActivity(), ServiceConnection {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private val application = GPSApplication.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.last}]")
        super.onCreate(savedInstanceState)
        application.activity = this
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.last}]")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${application.last}]")
    }
}