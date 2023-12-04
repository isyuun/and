/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.1
 *
 */

package net.pettip.gps._app

import android.net.Uri
import net.pettip.gps.app.ICameraContentListener
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File

/**
 * @Project     : PetTip-Android
 * @FileName    : gpsapplication5
 * @Date        : 2023-12-01
 * @author      : isyuun
 * @description : net.pettip.gps._app
 * @see net.pettip.gps._app.gpsapplication4
 */
open class gpsapplication4 : gpsapplication3(), ICameraContentListener {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun camera() {
        Log.i(__CLASSNAME__, "${getMethodName()}::onCamera()")
        //var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //activity?.startActivity(intent/*.addFlags(FLAG_ACTIVITY_NEW_TASK)*/)
        if (activity is ICameraContentListener) (activity as ICameraContentListener).camera()
    }

    override fun onCamera(file: File, uri: Uri) {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.activity}][${(this.activity is ICameraContentListener)}][file:$file][uri:$uri]")
        if (this.activity is ICameraContentListener) (this.activity as ICameraContentListener).onCamera(file, uri)
    }

    fun img(uri: Uri) {
        service?.img(uri)
    }
}