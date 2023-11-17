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
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : PetTip-Android
 * @FileName    : gpsappcompatactivity4
 * @Date        : 2023-11-17
 * @author      : isyuun
 * @description : kr.carepet.gps._app
 * @see kr.carepet.gps._app.gpsappcompatactivity4
 */
open class gpsappcompatactivity4 : gpsappcompatactivity3() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    internal fun onChange(selfChange: Boolean, uri: Uri) {
        Log.w(__CLASSNAME__, "${getMethodName()}[selfChange:$selfChange][uri:$uri]")
    }
}