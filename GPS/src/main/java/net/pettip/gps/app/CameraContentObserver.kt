/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.11.23
 *
 */

package net.pettip.gps.app

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import net.pettip.gpx.GPX_SIMPLE_TICK_FORMAT
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File

/**
 * @Project     : carepet-android
 * @FileName    : CameraContentObserver.kt
 * @Date        : 2023. 09. 18.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
interface ICameraContentListener {
    //var takePicture: ActivityResultLauncher<Intent>
    fun camera()
    fun onChange(uri: Uri, file: File)
}

class CameraContentObserver(
    handler: Handler,
    private val resolver: ContentResolver,
    private val listener: ICameraContentListener,
) : ContentObserver(handler) {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun deliverSelfNotifications(): Boolean {
        val ret = super.deliverSelfNotifications()
        Log.v(__CLASSNAME__, "${getMethodName()}[${this.file == file}][this.uri:$this.uri][uri:$file]")
        return ret
    }

    fun path(uri: Uri): String? {
        var path: String? = null
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = resolver.query(uri, projection, null, null, null)
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

    fun time(uri: Uri): Long? {
        var time: Long? = null
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATE_MODIFIED)
            val cursor = resolver.query(uri, projection, null, null, null)
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

    fun camera(uri: Uri): Boolean {
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        val cursor = resolver.query(uri, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(projection[0])
                val bucketName = it.getString(columnIndex)
                return bucketName.contains("DCIM/Camera") || mime(uri)
            }
        }

        return false
    }

    fun mime(imageUri: Uri): Boolean {
        val mimeType = resolver.getType(imageUri)
        return mimeType?.startsWith("image/") == true
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
    private fun rotate(uri: Uri): ROTATE {
        val path = path(uri)
        val file = path?.let { File(it) }
        var rotate = ROTATE.ROTATE_NG
        var orientation: Int? = null
        try {
            resolver.notifyChange(uri, null)
            val exif = file?.let { ExifInterface(it.absolutePath) }
            if (exif != null) {
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate = ROTATE.ROTATE_270
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate = ROTATE.ROTATE_180
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate = ROTATE.ROTATE_90
                    ExifInterface.ORIENTATION_NORMAL -> rotate = ROTATE.ROTATE_0
                }
            }
            Log.i(__CLASSNAME__, "${getMethodName()}::onChange()[orientation:$orientation][rotate:$rotate][uri:$uri][path:$path][file:${file?.absolutePath}]")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return rotate
    }

    /**
     * 카메라 이미지 회전방향: 컨텐츠리졸버(DB)사용
     */
    private fun orient(uri: Uri): ROTATE {
        val path = path(uri)
        val file = path?.let { File(it) }
        var rotate = ROTATE.ROTATE_NG
        var orientation: Int = -1
        try {
            val cursor = resolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null)
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
            Log.w(__CLASSNAME__, "${getMethodName()}::onChange()[orientation:$orientation][rotate:$rotate][uri:$uri][path:$path][file:${file?.absolutePath}]")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rotate
    }

    private var file: File? = null
    private fun onChange(uri: Uri) {
        //Log.i(__CLASSNAME__, "${getMethodName()}[${this.uri == uri}][this.uri:$this.uri][uri:$uri]")
        val path = path(uri) ?: return
        val time = time(uri) ?: return
        val file = File(path)
        val name = file.name
        val exists = file.exists()
        val camera = exists && camera(uri) && !name.startsWith(".")
        if (camera) {
            if (this.file == file) return
            val rotate = rotate(uri)
            val orient = orient(uri)
            Log.wtf(__CLASSNAME__, "${getMethodName()}[${(this.file == file)}][$camera][rotate:$rotate][orient:$orient][$name][file:$file][time:$time.${time.let { GPX_SIMPLE_TICK_FORMAT.format(it) }}]")
            uri.let { listener.onChange(it, file) }
            this.file = file
        }
    }

    override fun onChange(selfChange: Boolean) {
        Log.v(__CLASSNAME__, "${getMethodName()}[$selfChange]")
        super.onChange(selfChange)
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        Log.v(__CLASSNAME__, "${getMethodName()}[$selfChange][uri:$uri]")
        super.onChange(selfChange, uri)
        uri?.let { onChange(it) }
    }

    override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
        Log.v(__CLASSNAME__, "${getMethodName()}[$selfChange][uri:$uri][flags:$flags]")
        super.onChange(selfChange, uri, flags)
        uri?.let { onChange(it) }
    }

    override fun onChange(selfChange: Boolean, uris: MutableCollection<Uri>, flags: Int) {
        Log.v(__CLASSNAME__, "${getMethodName()}[$selfChange][uris:$uris][flags:$flags]")
        super.onChange(selfChange, uris, flags)
        uris.forEach { uri ->
            uri.let { onChange(it) }
        }
    }
}