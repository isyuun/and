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
 *  isyuun@care-pet.kr             2023. 9. 18.   description...
 */

package kr.carepet.gps

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import kr.carepet.gpx.GPX_SIMPLE_TICK_FORMAT
import kr.carepet.util.Log
import kr.carepet.util.getMethodName
import java.io.File

/**
 * @Project     : carepet-android
 * @FileName    : CameraContentObserver.kt
 * @Date        : 2023. 09. 18.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
class CameraContentObserver(private val context: Context, handler: Handler) : ContentObserver(handler) {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        //Log.d(__CLASSNAME__, "${getMethodName()}: $uri")
        if (uri != null) {
            val path = path(uri)
            val time = time(uri)
            if (path == null || time == null) return
            val file = File(path)
            val name = file.name
            Log.i(__CLASSNAME__, "${getMethodName()}[${name?.let { it }}][exist:${file?.let { file.exists() }}]: path:${path?.let { it }}, time:${time?.let { GPX_SIMPLE_TICK_FORMAT.format(it) }}")
        }
    }

    private fun path(uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
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
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(projection[0])
                time = it.getLong(columnIndex)
            }
        }
        return time
    }
}