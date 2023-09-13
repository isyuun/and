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
 *  isyuun@care-pet.kr             2023. 9. 6.   description...
 */

package kr.carepet._gps

/**import kr.carepet.util.__CLASSNAME__*/
import android.location.Location
import android.os.Environment
import com.google.android.gms.location.LocationResult
import kr.carepet.gpx.GPXWriter2
import kr.carepet.util.Log
import kr.carepet.util.getMethodName
import java.io.File
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date


const val LATITUDE_ZERO_KO = 127.054136
const val LONGITUDE_ZERO_KO = 37.275935

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice2.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class foregroundonlylocationservice2 : foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private val locations = Collections.synchronizedList(ArrayList<Location>()) // The list of Tracks

    /** <a hreef="https://stackoverflow.com/questions/43080343/calculate-distance-between-two-locations-in-metre">Calculate distance between two locations in metre</a> */
    //https://stackoverflow.com/questions/43080343/calculate-distance-between-two-locations-in-metre
    private fun distance(loc1: Location?, loc2: Location?): Float {
        if (loc1 == null || loc2 == null) return 0.0f
        val lat1: Double = loc1.latitude
        val lon1: Double = loc1.longitude
        val lat2: Double = loc2.latitude
        val lon2: Double = loc2.longitude
        val distance = FloatArray(2)
        Location.distanceBetween(
            lat1, lon1,
            lat2, lon2, distance
        )
        return distance[0]
    }

    override fun onLocationResult(locationResult: LocationResult) {
        val loc1 = currentLocation
        val loc2 = locationResult.lastLocation
        val dist = distance(loc1, loc2)
        val size = locations.size
        val exit = (size > 0 && dist < 2.0f)
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${exit}][$size][${dist}.m][${loc1.toText()}, ${loc2.toText()}], $loc1, $loc2")
        if (exit) return
        super.onLocationResult(locationResult)
        locations.add(currentLocation)
    }

    override fun onCreate() {
        super.onCreate()

        /* 내부저장소 */
        // 캐시(Cache)
        val fileCacheDir = cacheDir
        val getCacheDir = fileCacheDir.path
        Log.i(__CLASSNAME__, "${getMethodName()}$getCacheDir")
        // 데이터베이스(Database)
        val fileDataBase = getDatabasePath("Android")
        val getDatabasePath = fileDataBase.path
        Log.i(__CLASSNAME__, "${getMethodName()}$getDatabasePath")
        // 일반 파일
        val fileFile = filesDir
        val getFile = fileFile.path
        Log.i(__CLASSNAME__, "${getMethodName()}$getFile")
        // 일반 파일 폴더
        val fileFileName = getFileStreamPath("Android")
        val getFileName = fileFileName.path
        Log.i(__CLASSNAME__, "${getMethodName()}$getFileName")
        /* 외부저장소 - 공용 영역 */
        // 최상위 경로
        val getDirectory = Environment.getExternalStorageDirectory().toString() + "/tmp"
        Log.i(__CLASSNAME__, "${getMethodName()}$getDirectory")
        // 특정 데이터를 저장
        val fileAlarms = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val getAlarms = fileAlarms.path
        Log.i(__CLASSNAME__, "${getMethodName()}$getAlarms")
        /* 외부저장소 - 어플리케이션 고유 영역 */
        // 특정 데이터를 저장
        val fileAlarms2 = getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS)
        val getAlarms2 = fileAlarms2[0].path
        Log.i(__CLASSNAME__, "${getMethodName()}$getAlarms2")
        // 캐시 데이터를 저장
        val getCache2 = externalCacheDir.toString() + "/tmp"
        Log.i(__CLASSNAME__, "${getMethodName()}$getCache2")
    }

    private var tick = ""

    private fun tick() {
        tick = SimpleDateFormat("yyyyMMdd.HHmmss", resources.configuration.locales[0]).format(Date(System.currentTimeMillis()))
    }

    override fun start() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}${locations.size}")
        super.start()
        tick()
    }

    private fun write() {
        //val path = externalCacheDir
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File("$path/.GPX/$tick.gpx")
        file.parentFile.mkdirs()
        Log.wtf(__CLASSNAME__, "${getMethodName()}${locations.size}, $file")
        GPXWriter2.write(locations, file)
        locations.clear()
    }

    override fun stop() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}${locations.size}")
        super.stop()
        write()
    }
}