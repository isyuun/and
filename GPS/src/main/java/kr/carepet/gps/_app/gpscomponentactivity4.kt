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

package kr.carepet.gps._app

import android.net.Uri
import kr.carepet.gps.app.ICameraContentObserver
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : PetTip-Android
 * @FileName    : gpscomponentactivity4
 * @Date        : 2023-11-17
 * @author      : isyuun
 * @description : kr.carepet.gps._app
 * @see kr.carepet.gps._app.gpscomponentactivity4
 */
open class gpscomponentactivity4 : gpscomponentactivity3(), ICameraContentObserver {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun onCameraChange(selfChange: Boolean, uri: Uri) {
        Log.v(__CLASSNAME__, "${getMethodName()}[selfChange:$selfChange]")
    }
}