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
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider.*
import net.pettip.gps.app.ICameraContentListener
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @Project     : PetTip-Android
` * @FileName    : gpscomponentactivity4
 * @Date        : 2023-11-17
 * @author      : isyuun
 * @description : net.pettip.gps._app
 * @see net.pettip.gps._app.gpscomponentactivity4
 */
open class gpscomponentactivity4 : gpscomponentactivity3(), ICameraContentListener {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    lateinit var file: File
    lateinit var uri: Uri

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val time: String = SimpleDateFormat("yyyyMMdd.HHmmss", Locale.KOREA).format(Date())
        val path: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "IMG.${time}", /* prefix */
            ".jpg", /* suffix */
            path /* directory */
        )
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        Log.wtf(__CLASSNAME__, "${getMethodName()}::onChange()[uri:$uri][file:$file]")
        if (isSuccess) {
            onChange(uri, file)
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.wtf(__CLASSNAME__, "${getMethodName()}::onChange()[result.data:$result.data]")
        if (result.resultCode == Activity.RESULT_OK) {
            onChange(uri, file)
        }
    }

    override fun camera() {
        //val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //takePicture.launch(intent)
        try {
            file = createImageFile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (file.exists()) {
            uri = getUriForFile(this, "${packageName}.provider", file)
            cameraLauncher.launch(uri)
        }
    }

    override fun onChange(uri: Uri, file: File) {
        Log.v(__CLASSNAME__, "${getMethodName()}[uri:$uri][file:$file]")
    }

}