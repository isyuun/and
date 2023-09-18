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
import android.os.Environment
import com.google.android.gms.location.LocationResult
import kr.carepet.gpx.GPXWriter2
import kr.carepet.gpx.GPX_SIMPLE_TICK_FORMAT
import kr.carepet.gpx.Location
import kr.carepet.util.Log
import kr.carepet.util.getMethodName
import java.io.File
import java.util.Collections

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice2.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class foregroundonlylocationservice2 : foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate() {
        super.onCreate()
        /* 내부저장소 */
        // 캐시(Cache)
        val fileCacheDir = cacheDir
        val getCacheDir = fileCacheDir.path
        Log.i(__CLASSNAME__, "${getMethodName()}$getCacheDir")
        // 데이터베이스(Database)
        val fileDataBase = getDatabasePath("...")
        val getDatabasePath = fileDataBase.path
        Log.i(__CLASSNAME__, "${getMethodName()}$getDatabasePath")
        // 일반 파일
        val fileFile = filesDir
        val getFile = fileFile.path
        Log.i(__CLASSNAME__, "${getMethodName()}$getFile")
        // 일반 파일 폴더
        val fileFileName = getFileStreamPath("...")
        val getFileName = fileFileName.path
        Log.i(__CLASSNAME__, "${getMethodName()}$getFileName")
        /* 외부저장소 - 공용 영역 */
        // 최상위 경로
        val getDirectory = Environment.getExternalStorageDirectory().toString()
        Log.i(__CLASSNAME__, "${getMethodName()}$getDirectory")
        // 특정 데이터를 저장
        val fileDowns = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val getDowns = fileDowns.path
        Log.i(__CLASSNAME__, "${getMethodName()}$getDowns")
        /* 외부저장소 - 어플리케이션 고유 영역 */
        // 특정 데이터를 저장
        val fileDowns2 = getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS)
        val getDowns2 = fileDowns2[0].path
        Log.i(__CLASSNAME__, "${getMethodName()}$getDowns2")
        // 캐시 데이터를 저장
        val getCache2 = externalCacheDir.toString()
        Log.i(__CLASSNAME__, "${getMethodName()}$getCache2")
    }

    private val locations = Collections.synchronizedList(ArrayList<Location>()) // The list of Tracks

    /** <a hreef="https://stackoverflow.com/questions/43080343/calculate-distance-between-two-locations-in-metre">Calculate distance between two locations in metre</a> */
    //https://stackoverflow.com/questions/43080343/calculate-distance-between-two-locations-in-metre
    private fun distance(loc1: Location?, loc2: Location?): Float {
        if (loc1 == null || loc2 == null) return 0.0f
        val lat1: Double = loc1.latitude
        val lon1: Double = loc1.longitude
        val lat2: Double = loc2.latitude
        val lon2: Double = loc2.longitude
        val distances = FloatArray(2)
        Location.distanceBetween(
            lat1, lon1,
            lat2, lon2,
            distances
        )
        return distances[0]
    }

    protected fun add(location: Location) {
        locations.add(location)
    }

    override fun onLocationResult(locationResult: LocationResult) {
        val loc1 = currentLocation?.let { Location(it) }
        val loc2 = locationResult.lastLocation?.let { Location(it) }
        val dist = distance(loc1, loc2)
        val size = locations.size
        val exit = (size > 0 && dist < 2.0f)
        Log.wtf(__CLASSNAME__, "${getMethodName()}[exit:$exit][$size][${dist}.m][${loc1?.toText()}, ${loc2?.toText()}], $loc1, $loc2")
        if (exit) {
            currentLocation = locationResult.lastLocation
            dump()
            return
        }
        super.onLocationResult(locationResult)
        currentLocation?.let { add(Location(it)) }
        dump()
    }

    protected fun dump() {
        if (start > 0) write(false)
    }

    private fun write(clear: Boolean) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val time = locations.first().location.time
        val file = File("$path/.GPX/${GPX_SIMPLE_TICK_FORMAT.format(time)}.gpx")
        file.parentFile?.mkdirs()
        Log.wtf(__CLASSNAME__, "${getMethodName()}$clear, ${locations.size}, ${GPX_SIMPLE_TICK_FORMAT.format(this.start)}, ${GPX_SIMPLE_TICK_FORMAT.format(time)}, $file")
        GPXWriter2.write(locations, file)
        if (clear) locations.clear()
    }

    fun pee(id: String = "") {
        Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val location = currentLocation?.let { Location(it, id, 1, 0) }
        location?.let { add(it) }
        dump()
    }

    fun poo(id: String = "") {
        Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val location = currentLocation?.let { Location(it, id, 0, 1) }
        location?.let { add(it) }
        dump()
    }

    private var start = 0L

    override fun start() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}${currentLocation.toText()}, $currentLocation")
        super.start()
        start = System.currentTimeMillis() /*GPX_SIMPLE_TICK_FORMAT.format(Date(System.currentTimeMillis()))*/
    }

    override fun stop() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}${currentLocation.toText()}, $currentLocation")
        super.stop()
        if (locations.size > 0) write(true)
        start = 0L
    }
}
