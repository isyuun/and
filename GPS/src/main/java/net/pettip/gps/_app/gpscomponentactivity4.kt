/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.11.17
 *
 */

package net.pettip.gps._app

import android.net.Uri
import net.pettip.gps.app.ICameraContentObserver
import net.pettip.util.Log
import net.pettip.util.getMethodName

/**
 * @Project     : PetTip-Android
 * @FileName    : gpscomponentactivity4
 * @Date        : 2023-11-17
 * @author      : isyuun
 * @description : net.pettip.gps._app
 * @see net.pettip.gps._app.gpscomponentactivity4
 */
open class gpscomponentactivity4 : gpscomponentactivity3(), ICameraContentObserver {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun onCameraChange(selfChange: Boolean, uri: Uri) {
        Log.v(__CLASSNAME__, "${getMethodName()}[selfChange:$selfChange]")
    }
}