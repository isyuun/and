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
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.gps._app

import android.Manifest
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.ServiceConnection
import android.media.ExifInterface
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import kr.carepet.gps.app.CameraContentObserver
import kr.carepet.gpx.GPX_SIMPLE_TICK_FORMAT
import kr.carepet.gpx.Track
import kr.carepet.util.Log
import kr.carepet.util.getMethodName
import java.io.File
import java.util.Collections


/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice3.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class foregroundonlylocationservice4 : foregroundonlylocationservice3(), ServiceConnection {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
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

    private fun path(uri: Uri): String {
        var path = ""
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

    private fun time(uri: Uri): Long? {
        var time: Long? = null
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATE_ADDED)
            val cursor = contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(projection[0])
                    time = it.getLong(columnIndex)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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


    override fun start() {
        super.start()
        _imgs.clear()
    }

    private fun img(uri: Uri) {
        if (_imgs.size > 0 && _imgs.contains(uri)) return
        _imgs.add(uri)
        val loc = lastLocation
        val img = _imgs.size
        val trk = loc?.let { Track(it/*, no = this.no*/, img = img, uri = uri) }
        Log.w(__CLASSNAME__, "${getMethodName()}[$img, ${_imgs.size}], ${_imgs[img - 1]}, $trk")
        trk?.let { _tracks.add(it) }
        write()
    }

    enum class ROTATE {
        ROTATE_NG,
        ROTATE_0,
        ROTATE_90,
        ROTATE_180,
        ROTATE_270,
    }

    internal fun rotate(context: Context, uri: Uri): ROTATE {
        val path = path(uri)
        val file = File(path)
        var rotate = ROTATE.ROTATE_NG
        var orientation: Int
        try {
            context.contentResolver.notifyChange(uri, null)
            val exif = ExifInterface(file.absolutePath)
            //val exif = ExifInterface(path)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = ROTATE.ROTATE_270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = ROTATE.ROTATE_180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = ROTATE.ROTATE_90
                ExifInterface.ORIENTATION_NORMAL -> rotate = ROTATE.ROTATE_0
            }
            Log.i(__CLASSNAME__, "${getMethodName()}::onChange()[orientation:$orientation][rotate:$rotate][uri:$uri][path:$path][file:${file.absolutePath}]")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return rotate
    }

    internal fun orient(context: Context, uri: Uri): ROTATE {
        val path = path(uri)
        val file = File(path)
        var rotate = ROTATE.ROTATE_NG
        var orientation: Int = -1
        try {
            var cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null)
            cursor?.let {
                if (it.count == 1) {
                    it.moveToFirst()
                    orientation = it.getInt(0)
                    when (orientation) {
                        270 -> rotate = ROTATE.ROTATE_270
                        180 -> rotate = ROTATE.ROTATE_180
                        90 -> rotate = ROTATE.ROTATE_90
                        0 -> rotate = ROTATE.ROTATE_0
                    }
                }
                it.close()
            }
            Log.w(__CLASSNAME__, "${getMethodName()}::onChange()[orientation:$orientation][rotate:$rotate][uri:$uri][path:$path][file:${file.absolutePath}]")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rotate
    }

    fun onChange(selfChange: Boolean, uri: Uri) {
        val path = path(uri)
        val time = time(uri) ?: return
        //Log.d(__CLASSNAME__, "${getMethodName()}$selfChange, $uri, $path, $time")
        val file = File(path)
        val name = file.name
        val exists = file.exists()
        val camera = exists && camera(uri) && !_imgs.contains(uri) && !name.startsWith(".")
        if (camera) {
            val rotate = rotate(this, uri)
            val orient = orient(this, uri)
            Log.wtf(__CLASSNAME__, "${getMethodName()}[$selfChange][camera:$camera][orient:$orient][rotate:$rotate][$name][path:$path][time:${time.let { GPX_SIMPLE_TICK_FORMAT.format(it) }}]")
            img(uri)
        }
    }

    private val _imgs = Collections.synchronizedList(ArrayList<Uri>()) // The list of Tracks
    internal val images
        get() = _imgs
}