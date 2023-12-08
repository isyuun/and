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

import net.pettip.gpx.GPXParser
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
open class foregroundonlylocationservice6 : foregroundonlylocationservice5() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun onCreate() {
        super.onCreate()
        read()  //test
        last()
    }

    private fun last() {
        //TODO("Not yet implemented")
    }

    private fun read() {
        val path = File(this.path)
        var file: File? = null
        var last = 0L
        //Log.v(__CLASSNAME__, "${getMethodName()}[${path.listFiles()}][${path}]")
        path.listFiles()?.forEach {
            //println(it)
            if (last < it.lastModified()) file = it
            last = it.lastModified()
        }
        Log.v(__CLASSNAME__, "${getMethodName()}[${file}]")
        file?.let { read(it) }
    }

    private fun read(file: File) {
        Log.w(__CLASSNAME__, "${getMethodName()}$file, $_tracks")
        GPXParser(_tracks).read(file)
        Log.v(__CLASSNAME__, "${getMethodName()}[_tracks.size:${_tracks.size}]")
        //_tracks.forEach {
        //    Log.w(__CLASSNAME__, "${getMethodName()}[${GPX_DATE_FORMAT.format(it.time)}]$it")
        //}
    }

}