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

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import net.pettip.gps.app.GPSApplication
import net.pettip.gps.app.ICameraContentListener
import net.pettip.gpx.GPX_TICK_FORMAT
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File
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

    private val application = GPSApplication.instance
    private var file: File? = null
    private var uri: Uri? = null

    fun path(uri: Uri): String? {
        var path: String? = null
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(projection[0])
                    path = it.getString(columnIndex)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return path
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        this.file = this.uri?.let { path(it) }?.let { File(it) }
        val size = file?.length()
        if (size != null) {
            if (success && size > 0) {
                Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[success:$success][size:$size][uri:${this.uri}][file:${this.file}]")
                this.uri?.let { application.img(it) }
            }
        }
    }

    override fun camera() {
        ///**버전.38*/
        this.uri = createImageUri()
        cameraLauncher.launch(this.uri)
    }

    private fun createImageUri(): Uri? {
        val time = System.currentTimeMillis()
        val name = GPX_TICK_FORMAT.format(Date(time))
        ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, name)
            put(MediaStore.Images.Media.DESCRIPTION, "Photo taken by PetTip")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            val ret = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10 이상에서는 getContentUri 함수 사용
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/PetTip")
                contentResolver.insert(MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), this)
            } else {
                // Android 9 이하에서는 DATA를 사용
                val path = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath}${File.separator}PetTip${File.separator}$name.jpg"
                put(MediaStore.Images.Media.DATA, path)
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this)
            }
            Log.v(__CLASSNAME__, "${getMethodName()}::onCamera()[Q:${(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)}][ret:$ret]")
            return ret
        }
    }

    override fun onCamera(file: File, uri: Uri) {
        if (file.length() < 1) return
        if (this.file != null && this.file != file) {
            val delete = this.file?.delete()
            Log.w(__CLASSNAME__, "${getMethodName()}::onCamera()[delete:$delete][file:$file][this.file:${this.file}]")
        }
        if (file.exists() && file.length() > 0) {
            Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
            application.img(uri)
            return
        }
        Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[exists:${file.exists()}][length:${file.length()}][name:${file.name}][$file]")
    }
}