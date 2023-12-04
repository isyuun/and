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
import net.pettip.gps.app.ICameraContentListener
import java.io.File

/**
 * @Project     : PetTip-Android
 * @FileName    : gpsappcompatactivity4
 * @Date        : 2023-11-17
 * @author      : isyuun
 * @description : net.pettip.gps._app
 * @see net.pettip.gps._app.gpsappcompatactivity4
 */
open class gpsappcompatactivity4 : gpsappcompatactivity3(), ICameraContentListener {
    override fun camera() {
        //TODO("Not yet implemented")
    }

    override fun onCamera(file: File, uri: Uri) {
        //TODO("Not yet implemented")
    }
}