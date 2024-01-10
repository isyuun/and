/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 20.   description...
 */

package net.pettip.gps._app

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import net.pettip.gpx.Track
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File
import java.util.Collections


/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice3.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class foregroundonlylocationservice4() : foregroundonlylocationservice3(), ServiceConnection {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
    }

    protected val _images = Collections.synchronizedList(ArrayList<Uri>()) // The list of Tracks
    internal val images
        get() = _images


    private fun exist(uri: Uri): Boolean {
        if (_images.size > 0 && _images.contains(uri)) return true
        var ret = false
        val file = path(uri)?.let { File(it) }
        if (file != null) _images.forEach {
            val _file = path(it)?.let { File(it) }
            if (file == _file) ret = true
        }
        Log.w(__CLASSNAME__, "${getMethodName()}[$uri][$file][$ret]")
        return ret
    }

    internal fun img(uri: Uri) {
        if (exist(uri)) return
        _images.add(uri)
        val loc = lastLocation
        val img = _images.size
        val trk = loc?.let { Track(it/*, no = this.no*/, event = Track.EVENT.IMG, uri = uri) }
        Log.w(__CLASSNAME__, "${getMethodName()}[$img, ${_images.size}], ${_images[img - 1]}, $trk")
        trk?.let { _tracks.add(it) }
        write()
    }

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

    enum class ROTATE {
        ROTATE_NG,
        ROTATE_0,
        ROTATE_90,
        ROTATE_180,
        ROTATE_270,
    }

    /**
     * 카메라 이미지 회전방향: ExifInterface사용
     */
    internal fun rotate(context: Context, uri: Uri): ROTATE {
        val path = path(uri) ?: return ROTATE.ROTATE_NG
        val file = File(path)
        var rotate = ROTATE.ROTATE_NG
        val orientation: Int
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
            Log.i(__CLASSNAME__, "${getMethodName()}::onCameraChange()[orientation:$orientation][rotate:$rotate][uri:$uri][path:$path][file:${file.absolutePath}]")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return rotate
    }

    /**
     * 카메라 이미지 회전방향: 컨텐츠리졸버(DB)사용
     */
    internal fun orient(context: Context, uri: Uri): ROTATE {
        val path = path(uri) ?: return ROTATE.ROTATE_NG
        val file = File(path)
        var rotate = ROTATE.ROTATE_NG
        var orientation: Int = -1
        try {
            val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null)
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
            Log.w(__CLASSNAME__, "${getMethodName()}::onCameraChange()[orientation:$orientation][rotate:$rotate][uri:$uri][path:$path][file:${file.absolutePath}]")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rotate
    }
}