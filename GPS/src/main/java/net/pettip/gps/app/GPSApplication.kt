/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 20.   description...
 */

package net.pettip.gps.app

import android.Manifest
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import net.pettip.gps._app.KEY_FOREGROUND_GPXFILE
import net.pettip.gps._app.gpsapplication4
import net.pettip.map.app.GpxActivity
import net.pettip.map.app.MapActivity
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File

/**
 * @Project     : carepet-android
 * @FileName    : GPSApplication.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class GPSApplication : gpsapplication4() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    companion object {
        private var singleton: GPSApplication? = null

        val instance
            get() = isntance()

        @JvmStatic
        private fun isntance(): GPSApplication {
            return singleton ?: synchronized(this) {
                singleton ?: GPSApplication().also {
                    singleton = it
                }
            }
        }

        internal val permissions: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            arrayOf(
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.FOREGROUND_SERVICE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            arrayOf(
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }
    }

    override fun onCreate() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.onCreate()
        singleton = this
    }


    fun openMap() {
        val intent = Intent(this, MapActivity::class.java).addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun openGpx(file: File) {
        val intent = Intent(this, GpxActivity::class.java).addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(KEY_FOREGROUND_GPXFILE, file.absolutePath)
        startActivity(intent)
    }
}