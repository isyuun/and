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

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import net.pettip.gps.app.ICameraContentListener
import net.pettip.util.Log
import net.pettip.util.getMethodName
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
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    val takePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.wtf(__CLASSNAME__, "${getMethodName()}::onChange()[result:$result]")
        if (result.resultCode == Activity.RESULT_OK) {
            //val data: Intent? = result.data
            //// Process the captured image data
            //// For example, you can retrieve the image from the data Intent and update the state
            //val thumbnail = data?.extras?.get("data") as? android.graphics.Bitmap
            //imageBitmap = thumbnail
        }
    }

    override fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePicture.launch(intent)
    }

    override fun onChange(uri: Uri, file: File) {
        Log.v(__CLASSNAME__, "${getMethodName()}[uri:$uri][file:$file]")
    }
}