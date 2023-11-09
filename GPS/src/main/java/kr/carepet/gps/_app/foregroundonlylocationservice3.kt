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
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.gps._app

/**import kr.carepet.util.__CLASSNAME__*/
import android.content.Intent
import android.location.Location
import com.google.android.gms.location.LocationResult
import kr.carepet.gpx.GPXWriter2
import kr.carepet.gpx.GPX_INTERVAL_UPDATE_METERS
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

open class foregroundonlylocationservice3 : foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    /** <a hreef="https://stackoverflow.com/questions/43080343/calculate-distance-between-two-tracks-in-metre">Calculate distance between two tracks in metre</a> */
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

    protected fun exit(locationResult: LocationResult): Boolean {
        val trk1 = lastLocation?.let { Track(it) }
        val trk2 = locationResult.lastLocation?.let { Track(it) }
        val dist = distance(trk1, trk2)
        val size = _tracks.size
        val max = GPX_INTERVAL_UPDATE_METERS
        val exit = (size > 0 && dist < max)
        Log.v(__CLASSNAME__, "::onLocationResult${getMethodName()}[exit:$exit][$size][${max}m][${dist}m][${trk1?.toText()}, ${trk2?.toText()}], $trk1, $trk2")
        return exit
    }

    override fun onLocationResult(locationResult: LocationResult) {
        val exit = exit(locationResult)
        Log.wtf(__CLASSNAME__, "${getMethodName()}[exit:$exit]$lastLocation$locationResult")
        lastLocation = locationResult.lastLocation
        if (exit) return
        super.onLocationResult(locationResult)
        lastLocation?.let { _tracks.add(Track(it)) }
        /*if (_tracks.size == 1) */this.write()
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
        post { this.write() }
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
        super.onRebind(intent)
        post { this.write() }
    }


    private fun path(): String {
        //val ret = "${filesDir.path}/.GPX"
        val ret = getExternalFilesDirs(".GPX")[0].path
        Log.w(__CLASSNAME__, "${getMethodName()}$ret")
        return ret
    }

    override fun onCreate() {
        Log.d(__CLASSNAME__, "${getMethodName()}$_tracks")
        super.onCreate()
        _tracks.clear()
    }

    protected val _tracks = Collections.synchronizedList(ArrayList<Track>()) // The list of Tracks
    internal val tracks: MutableList<Track>
        get() = _tracks

    internal val path
        get() = path()

    internal val file
        get() = File("${path}/${GPX_SIMPLE_TICK_FORMAT.format(_tracks.first().time)}.gpx")

    protected fun write() {
        if (_tracks.isEmpty()) return
        val file = this.file
        file.parentFile?.mkdirs()
        Log.w(__CLASSNAME__, "${getMethodName()}$file, ${_tracks.first().time}")
        GPXWriter2.write(_tracks, file)
    }

    private var _no = ""
    internal var no: String
        get() = this._no
        set(no) {
            this._no = no
        }

    internal fun pee() {
        //Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val track = lastLocation?.let { Track(it, no = no, pee = 1) }
        track?.let { _tracks.add(it) }
        this.write()
    }

    internal fun poo() {
        //Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val track = lastLocation?.let { Track(it, no = no, poo = 1) }
        track?.let { _tracks.add(it) }
        this.write()
    }

    internal fun mrk() {
        //Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val track = lastLocation?.let { Track(it, no = no, mrk = 1) }
        track?.let { _tracks.add(it) }
        this.write()
    }

    private var _start = 0L
    val start
        get() = (_start > 0L) && _tracks.isNotEmpty()

    override fun start() {
        _start = System.currentTimeMillis()
        Log.wtf(__CLASSNAME__, "${getMethodName()}[$start][${lastLocation.toText()}][$lastLocation]")
        super.start()
        _tracks.clear()
    }

    override fun stop() {
        _start = 0L
        Log.wtf(__CLASSNAME__, "${getMethodName()}[$start][${lastLocation.toText()}][$lastLocation]")
        super.stop()
        this.write()
    }

    val __duration: String
        //get() = _GPXWriter.calculateDuration(_tracks)
        get() {
            if (_tracks.isEmpty()) {
                return "00:00:00"
            }
            val startTime = _tracks.first()?.time ?: System.currentTimeMillis()
            val endTime = System.currentTimeMillis()
            val durationInMillis = endTime - startTime
            val seconds = durationInMillis / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
        }

    val _duration: Long
        get() {
            if (_tracks.isEmpty()) {
                return 0L
            }
            val startTime = _tracks.first()?.time ?: System.currentTimeMillis()
            val endTime = _tracks.last()?.time ?: System.currentTimeMillis()
            return endTime - startTime
        }

    val duration: String
        //get() = _GPXWriter.calculateDuration(_tracks)
        get() {
            if (_tracks.isEmpty()) {
                return "00:00:00"
            }
            val startTime = _tracks.first()?.time ?: System.currentTimeMillis()
            val endTime = _tracks.last()?.time ?: System.currentTimeMillis()
            val durationInMillis = endTime - startTime
            val seconds = durationInMillis / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
        }

    val _distance: Float
        get() {
            if (_tracks.isEmpty()) {
                return 0.0f
            }
            var totalDistance = 0.0f
            _tracks.let {
                for (i in 1 until it.size) {
                    val prevLocation = it[i - 1]
                    val currentLocation = it[i]
                    val distance = prevLocation.location.distanceTo(currentLocation.location)
                    totalDistance += distance
                }
            }
            return totalDistance
        }

    val distance: String
        //get() = _GPXWriter.calculateTotalDistance(_tracks)
        get() {
            if (_tracks.isEmpty()) {
                return "0.00 km"
            }
            var totalDistance = 0.0
            _tracks.let {
                for (i in 1 until it.size) {
                    val prevLocation = it[i - 1]
                    val currentLocation = it[i]
                    val distance = prevLocation.location.distanceTo(currentLocation.location)
                    totalDistance += distance
                }
            }
            return String.format("%.2f km", totalDistance / 1000)
        }
}
