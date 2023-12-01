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
        Log.i(__CLASSNAME__, "::NaverMapApp@CAM::onChange()")
        //var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        ////if (true || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        ////    val ri = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        ////    val pm: PackageManager = context.packageManager
        ////    val ai = pm.resolveActivity(ri, 0)?.activityInfo
        ////    intent = Intent()
        ////    intent.component = ai?.let { ComponentName(it.packageName, it.name) }
        ////    intent.action = Intent.ACTION_MAIN
        ////    intent.addCategory(Intent.CATEGORY_LAUNCHER)
        ////    Log.w(__CLASSNAME__, "::NaverMapApp@CAM::onChange()(...)[intent:$intent][pm:$pm][ai:$ai]")
        ////}
        //startActivity(intent)
        if (activity is ICameraContentListener) (activity as ICameraContentListener).camera()
    }

    override fun onChange(uri: Uri, file: File) {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.activity}][${(this.activity is ICameraContentListener)}][uri:$uri][file:$file]")
        if (this.activity is ICameraContentListener) (this.activity as ICameraContentListener).onChange(uri, file)
    }
}