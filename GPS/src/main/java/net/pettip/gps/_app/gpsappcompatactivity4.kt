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
        //return File.createTempFile(
        //    "IMG.${name}.", /* prefix */
        //    ".jpg", /* suffix */
        //    path /* directory */
        //)
        val file = File("$path/IMG.$name.jpg")
        file.createNewFile()
        //Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
        return file
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        Log.w(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
        if (isSuccess) {
            onCamera(file, uri)
        }
    }

    override fun camera() {
        try {
            file = File()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        if (file.exists()) {
            Log.v(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
            uri = FileProvider.getUriForFile(this, "${packageName}.provider", file)
            cameraLauncher.launch(uri)
            return
        }
        Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
    }

    override fun onCamera(file: File, uri: Uri) {
        if (file.length() < 1) return
        if (this.file != file) {
            val delete = this.file.delete()
            Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[delete:$delete][${file.exists()}][${this.file.exists()}[this.file:${this.file}]")
        }
        if (file.exists() && file.length() > 0) {
            Log.i(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
            GPSApplication.instance.img(uri)
            return
        }
        Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
    }

}