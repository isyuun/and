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
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import kr.carepet.gps.CameraContentObserver
import kr.carepet.util.Log
import kr.carepet.util.getMethodName


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
}