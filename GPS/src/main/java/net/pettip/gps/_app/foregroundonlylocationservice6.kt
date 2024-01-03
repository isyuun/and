/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.8
 *
 */

package net.pettip.gps._app

import android.content.SharedPreferences
import net.pettip.app.gpxs
import net.pettip.gpx.GPXParser
import net.pettip.gpx.TRACK_ZERO_URI
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File

/**
 * @Project     : PetTip-Android
 * @FileName    : foregroundonlylocationservice6
 * @Date        : 2023-12-08
 * @author      : isyuun
 * @description : net.pettip.gps._app
 * @see net.pettip.gps._app.foregroundonlylocationservice6
 */
open class foregroundonlylocationservice6 : foregroundonlylocationservice5(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_FILE_KEY, MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        //last()?.let { last -> read(last) }  //test
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.i(__CLASSNAME__, "${getMethodName()}[$sharedPreferences][$key]")
    }

    override fun start() {
        Log.v(__CLASSNAME__, "${getMethodName()}[${recent()}")
        super.start()
        //sharedPreferences.edit().putString(KEY_FOREGROUND_GPXFILE, null).apply()
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${recent()}")
    }

    override fun stop() {
        Log.v(__CLASSNAME__, "${getMethodName()}[${recent()}")
        super.stop()
        sharedPreferences.edit().putString(KEY_FOREGROUND_GPXFILE, null).apply()
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${recent()}]")
    }

    internal fun reset() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${recent()}]")
        sharedPreferences.edit().putString(KEY_FOREGROUND_GPXFILE, null).apply()
    }

    internal fun recent(): File? {
        val file = sharedPreferences.getString(KEY_FOREGROUND_GPXFILE, "")?.let { File(it) }
        //Log.v(__CLASSNAME__, "${getMethodName()}[${file}]")
        return (if (file != null && file.exists()) file else null)
    }

    override fun write() {
        val file: File? = this._file
        Log.v(__CLASSNAME__, "${getMethodName()}[${file}]")
        super.write()
        file?.let { sharedPreferences.edit().putString(KEY_FOREGROUND_GPXFILE, it.absolutePath).apply() }
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${recent()}]")
    }

    internal fun last(): File? {
        val path = File(gpxs(this))
        var file: File? = this._file
        var last = 0L
        //Log.v(__CLASSNAME__, "${getMethodName()}[${path}]")
        path.listFiles()?.forEach {
            if (last < it.lastModified()) file = it
            last = it.lastModified()
        }
        Log.v(__CLASSNAME__, "${getMethodName()}[${file}]")
        return file
    }

    internal fun read(file: File) {
        Log.w(__CLASSNAME__, "${getMethodName()}$file")
        _tracks.let { _tracks -> GPXParser(_tracks).read(file) }
        //Log.v(__CLASSNAME__, "${getMethodName()}[_tracks.size:${_tracks.size}]")
        images.clear()
        _tracks.forEach { track ->
            //Log.i(__CLASSNAME__, "${getMethodName()}[${(track.uri != TRACK_ZERO_URI)}][${GPX_DATE_FORMAT.format(track.time)}]$track")
            if (track.uri != TRACK_ZERO_URI) images.add(track.uri)

        }
    }
}