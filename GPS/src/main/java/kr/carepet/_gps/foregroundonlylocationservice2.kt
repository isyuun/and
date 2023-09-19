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
import android.content.Intent
import android.location.Location
import android.os.Environment
import com.google.android.gms.location.LocationResult
import kr.carepet.gpx.GPXWriter2
import kr.carepet.gpx.GPX_METERS_TO_UPDATE
import kr.carepet.gpx.GPX_SIMPLE_TICK_FORMAT
import kr.carepet.gpx.Track
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

    private val tracks = Collections.synchronizedList(ArrayList<Track>()) // The list of Tracks

    /** <a hreef="https://stackoverflow.com/questions/43080343/calculate-distance-between-two-tracks-in-metre">Calculate distance between two tracks in metre</a> */
    //https://stackoverflow.com/questions/43080343/calculate-distance-between-two-locations-in-metre
    private fun distance(trk1: Track?, trk2: Track?): Float {
        if (trk1 == null || trk2 == null) return 0.0f
        val lat1: Double = trk1.latitude
        val lon1: Double = trk1.longitude
        val lat2: Double = trk2.latitude
        val lon2: Double = trk2.longitude
        val distances = FloatArray(2)
        Location.distanceBetween(
            lat1, lon1,
            lat2, lon2,
            distances
        )
        return distances[0]
    }

    protected fun add(track: Track) {
        tracks.add(track)
    }

    override fun onLocationResult(locationResult: LocationResult) {
        val loc1 = currentLocation?.let { Track(it) }
        val loc2 = locationResult.lastLocation?.let { Track(it) }
        val dist = distance(loc1, loc2)
        val size = tracks.size
        val max = GPX_METERS_TO_UPDATE
        val exit = (size > 0 && dist < max)
        Log.wtf(__CLASSNAME__, "${getMethodName()}[exit:$exit][$size][${max}m][${dist}m][${loc1?.toText()}, ${loc2?.toText()}], $loc1, $loc2")
        currentLocation = locationResult.lastLocation
        if (exit) return
        super.onLocationResult(locationResult)
        currentLocation?.let { add(Track(it)) }
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
        post { dump() }
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
        super.onRebind(intent)
        post { dump() }
    }

    protected fun dump() {
        Log.e(__CLASSNAME__, "${getMethodName()}...")
        if (start > 0) write(false)
    }

    protected open fun write(clear: Boolean) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val time = (tracks.first()?.loc)?.time
        val file = File("$path/.GPX/${GPX_SIMPLE_TICK_FORMAT.format(time)}.gpx")
        file.parentFile?.mkdirs()
        Log.w(__CLASSNAME__, "${getMethodName()}$clear, ${tracks.size}, ${GPX_SIMPLE_TICK_FORMAT.format(this.start)}, ${GPX_SIMPLE_TICK_FORMAT.format(time)}, $file")
        GPXWriter2.write(tracks, file)
        if (clear) tracks.clear()
    }

    private var _id = ""
    internal var id: String
        get() = this._id
        set(id) {
            this._id = id
        }

    fun pee() {
        Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val track = currentLocation?.let { Track(it, id = id, pee = 1) }
        track?.let { add(it) }
        dump()
    }

    fun poo() {
        Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val track = currentLocation?.let { Track(it, id = id, poo = 1) }
        track?.let { add(it) }
        dump()
    }

    fun mark() {
        Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val track = currentLocation?.let { Track(it, id = id, mrk = 1) }
        track?.let { add(it) }
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
        if (tracks.size > 0) write(true)
        start = 0L
    }
}
