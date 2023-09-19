/*
 *  Copyright 2011 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 14.   description...
 */

package kr.carepet._gps

import android.Manifest
import android.app.Notification
import android.content.ComponentName
import android.content.ContentResolver
import android.content.ServiceConnection
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import kr.carepet.gps.CameraContentObserver
import kr.carepet.gpx.GPX_SIMPLE_TICK_FORMAT
import kr.carepet.gpx.Track
import kr.carepet.util.Log
import kr.carepet.util.getMethodName
import java.io.File


/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice3.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class foregroundonlylocationservice3 : foregroundonlylocationservice2(), ServiceConnection {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
    }

    override fun generateNotification(location: android.location.Location?): Notification? {
        val ret = super.generateNotification(location)
        Log.i(__CLASSNAME__, "${getMethodName()}${location.toText()}, $ret")
        return ret
    }

    private lateinit var cameraContentObserver: CameraContentObserver
    private val handler: Handler = Handler(Looper.getMainLooper())

    @RequiresPermission(anyOf = [Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_EXTERNAL_STORAGE])
    override fun onCreate() {
        super.onCreate()

        cameraContentObserver = CameraContentObserver(this, handler)
        val contentResolver: ContentResolver = contentResolver
        val cameraImageUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        contentResolver.registerContentObserver(
            cameraImageUri,
            true,
            cameraContentObserver
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(cameraContentObserver)
    }

    private fun path(uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(projection[0])
                path = it.getString(columnIndex)
            }
        }
        return path
    }

    private fun time(uri: Uri): Long? {
        var time: Long? = null
        val projection = arrayOf(MediaStore.Images.Media.DATE_ADDED)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(projection[0])
                time = it.getLong(columnIndex)
            }
        }
        return time
    }

    private fun camera(uri: Uri): Boolean {
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(projection[0])
                val bucketName = it.getString(columnIndex)
                return bucketName.contains("DCIM/Camera") || mime(uri)
            }
        }

        return false
    }

    private fun mime(imageUri: Uri): Boolean {
        val mimeType = contentResolver.getType(imageUri)
        return mimeType?.startsWith("image/") == true
    }

    private fun img(path: String) {
        if (imgs.size > 0 && imgs.contains(path)) return
        imgs.add(path)
        val loc = currentLocation
        val img = if (imgs.size > 0) imgs.size - 1 else -1
        val trk = loc?.let { Track(it, id = this.id, img = img) }
        Log.wtf(__CLASSNAME__, "${getMethodName()}[$img, ${imgs.size}], ${imgs[img]}, $trk")
        trk?.let { add(it) }
        dump()
    }

    fun onChange(selfChange: Boolean, uri: Uri?) {
        //Log.d(__CLASSNAME__, "${getMethodName()}: $uri")
        if (uri != null) {
            val path = path(uri)
            val time = time(uri)
            if (path == null || time == null) return
            val file = File(path)
            val name = file.name
            val exists = file.exists()
            val camera = (exists && !name.startsWith(".") && camera(uri))
            if (exists && !name.startsWith(".") && camera(uri)) {
                Log.i(__CLASSNAME__, "${getMethodName()}[$selfChange][camera:$camera][$name]: path:$path, time:${time?.let { GPX_SIMPLE_TICK_FORMAT.format(it) }}")
                img(path)
            }
        }
    }

    private var imgs = ArrayList<String>()
    override fun start() {
        super.start()
        imgs.clear()
    }

    override fun stop() {
        super.stop()
        imgs.clear()
    }
}