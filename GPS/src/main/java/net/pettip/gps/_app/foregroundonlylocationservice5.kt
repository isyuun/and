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
import android.content.ContentResolver
import android.content.Context
import android.content.ServiceConnection
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import net.pettip.gps.app.CameraContentObserver
import net.pettip.gps.app.GPSApplication
import net.pettip.gps.app.ICameraContentListener
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
open class foregroundonlylocationservice5() : foregroundonlylocationservice4(), ServiceConnection, ICameraContentListener {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
    }

    private lateinit var observer: CameraContentObserver
    private val handler: Handler = Handler(Looper.getMainLooper())

    //@RequiresPermission(anyOf = [Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_EXTERNAL_STORAGE])
    override fun onCreate() {
        super.onCreate()
        observer = CameraContentObserver(handler, contentResolver, this)
        val contentResolver: ContentResolver = contentResolver
        val cameraImageUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        contentResolver.registerContentObserver(
            cameraImageUri,
            true,
            observer
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(observer)
    }

    private val _imgs = Collections.synchronizedList(ArrayList<Uri>()) // The list of Tracks
    internal val images
        get() = _imgs

    override fun start() {
        super.start()
        _imgs.clear()
    }

    internal fun img(uri: Uri) {
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

    /**
     * 카메라 이미지 회전방향: ExifInterface사용
     */
    internal fun rotate(context: Context, uri: Uri): ROTATE {
        val path = observer.path(uri) ?: return ROTATE.ROTATE_NG
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
        val path = observer.path(uri) ?: return ROTATE.ROTATE_NG
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

    override fun camera() {
        if (application is ICameraContentListener) (application as ICameraContentListener).camera()
    }

    override fun onCamera(uri: Uri, file: File) {
        val camera = !_imgs.contains(uri)
        Log.wtf(__CLASSNAME__, "${getMethodName()}::onCamera()[uri:$uri][file:$file]")
        if (camera) {
            img(uri)
            GPSApplication.instance.onCamera(uri, file)
        }
    }
}