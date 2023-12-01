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
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import net.pettip.gps.app.GPSApplication
import net.pettip.gps.app.ICameraContentListener
import net.pettip.gpx.GPX_SIMPLE_TICK_FORMAT
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File
import java.io.IOException
import java.util.Date

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

    lateinit var file: File
    lateinit var uri: Uri

    @Throws(IOException::class)
    private fun File(): File {
        // Create an image file name
        val time = System.currentTimeMillis()
        val name = GPX_SIMPLE_TICK_FORMAT.format(Date(time))
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        path.mkdirs()
        return File.createTempFile(
            "IMG.${name}", /* prefix */
            ".jpg", /* suffix */
            path /* directory */
        )
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[uri:$uri][file:$file]")
        if (isSuccess) {
            onCamera(uri, file)
        }
    }

    override fun camera() {
        try {
            file = File()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        Log.v(__CLASSNAME__, "${getMethodName()}::onCamera()[file:$file]")
        if (file.exists()) {
            //uri = Uri.fromFile(file)
            uri = FileProvider.getUriForFile(this, "${packageName}.provider", file)
            cameraLauncher.launch(uri)
        }
    }

    override fun onCamera(uri: Uri, file: File) {
        Log.v(__CLASSNAME__, "${getMethodName()}::onCamera()[${(this.file.exists() && this.file != file)}][uri:$uri][file:$file]")
        if (this.file.exists() && file.exists() && this.file != file) {
            val ret = this.file.delete()
            Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[$ret][${file.exists()}][${this.file.exists()}[this.file:${this.file}]")
        }
        if (file.exists()) {
            GPSApplication.instance.img(uri)
        }
    }

}