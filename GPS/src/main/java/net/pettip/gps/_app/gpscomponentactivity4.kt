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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider.*
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
` * @FileName    : gpscomponentactivity4
 * @Date        : 2023-11-17
 * @author      : isyuun
 * @description : net.pettip.gps._app
 * @see net.pettip.gps._app.gpscomponentactivity4
 */
open class gpscomponentactivity4 : gpscomponentactivity3(), ICameraContentListener {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    var file: File? = null
    var uri: Uri? = null

    @Throws(IOException::class)
    private fun File(): File {
        // Create an image file name
        val time = System.currentTimeMillis()
        val name = GPX_SIMPLE_TICK_FORMAT.format(Date(time))
        //val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val path = GPSApplication.instance.pics?.let { File(it) }
        path?.mkdirs()
        val file = File("$path/IMG.$name.jpg")
        Log.v(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
        file.createNewFile()
        return file
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        val camera = isSuccess && (file?.exists() == true)
        if (camera) {
            Log.w(__CLASSNAME__, "${getMethodName()}::onCamera()[isSuccess:$isSuccess][file:$file][uri:$uri]")
            file?.let { file -> uri?.let { uri -> onCamera(file, uri) } }
        }
    }

    override fun camera() {
        try {
            file = File()
            file?.let { file -> uri = getUriForFile(this, "${packageName}.provider", file) }
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val camera = (file != null && file?.exists() == true && uri != null)
        if (camera) {
            Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file?.exists()}][length:${file?.length()}][name:${file?.name}][$file]")
            cameraLauncher.launch(uri)
        }
        Log.v(__CLASSNAME__, "${getMethodName()}::onCamera()[camera:$camera}][$file][$uri]")
    }

    override fun onCamera(file: File, uri: Uri) {
        if (file.length() < 1) return
        if (this.file != null && this.file != file) {
            val delete = this.file?.delete()
            Log.w(__CLASSNAME__, "${getMethodName()}::onCamera()[delete:$delete][file:$file][this.file:${this.file}]")
        }
        if (file.exists() && file.length() > 0) {
            Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
            GPSApplication.instance.img(uri)
            return
        }
        Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
    }

}