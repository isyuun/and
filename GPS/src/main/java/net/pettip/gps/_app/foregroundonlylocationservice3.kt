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

/**import net.pettip.util.__CLASSNAME__*/
import android.content.Intent
import android.location.Location
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.pettip.gps.R
import net.pettip.gpx.GPXWriter
import net.pettip.gpx.Track
import net.pettip.gpx._distance
import net.pettip.gpx._duration
import net.pettip.gpx.distance
import net.pettip.singleton.G
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File
import java.util.Collections

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice2.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */

open class foregroundonlylocationservice3 : foregroundonlylocationservice2() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    internal fun spd() = lastLocation?.speed ?: 0.0

    private var exit = false
    private var _exit: Track? = null
    private val _exits = Collections.synchronizedList(ArrayList<Track>()) // The list of Tracks

    protected fun exit(locationResult: LocationResult): Boolean {
        val size = _tracks.size
        val loc1 = lastLocation
        val loc2 = locationResult.lastLocation
        val trk1 = loc1?.let { Track(it) }
        val trk2 = loc2?.let { Track(it) }
        val dis = distance(trk1, trk2)
        val min = GPS_UPDATE_MIN_METERS
        val max = GPS_UPDATE_MAX_METERS
        val moc = moc(loc2)
        val gps = gps()
        val chk = gps || moc
        val ret = (size > 0 && (!chk || dis < min || dis > max))
        Log.w(__CLASSNAME__, "::onLocationResult${getMethodName()}[gps:$gps][moc:$moc][exit:$ret][$size][min:${min}m][max:${max}m][dis:${dis}m]\n[loc1:$loc1][spd:${loc1?.speed}]\n[loc2:$loc2][spd:${loc2?.speed}]")
        return ret
    }

    override fun onLocationResult(locationResult: LocationResult) {
        val exit = exit(locationResult)
        val moc = moc(locationResult.lastLocation)
        val gps = gps()
        Log.wtf(__CLASSNAME__, "${getMethodName()}[gps:$gps][moc:$moc][exit:$exit]")
        lastLocation = locationResult.lastLocation
        if (!exit) {
            super.onLocationResult(locationResult)
            lastLocation?.let { _tracks.add(Track(it)) }
        }
        this.write()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
        post { this.write() }
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
        super.onRebind(intent)
        post { this.write() }
    }

    override fun start() {
        _start = System.currentTimeMillis()
        Log.wtf(__CLASSNAME__, "${getMethodName()}[$start][${lastLocation.toText()}][$lastLocation]")
        super.start()
        this.write()
    }

    override fun stop() {
        _start = 0L
        Log.wtf(__CLASSNAME__, "${getMethodName()}[$start][${lastLocation.toText()}][$lastLocation]")
        super.stop()
        this.write()
    }

    protected open fun write() {
        if (_tracks.isEmpty()) return
        val file = this._file
        file?.parentFile?.mkdirs()
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${date(_tracks.first().time)}]$file")
        file?.let { GPXWriter().write(_tracks, it) }
    }

    protected open fun delete() {
        if (_tracks.isEmpty()) return
        val file = this._file
        file?.parentFile?.mkdirs()
        file?.let { GPXWriter().delete(_tracks, it) }
    }

    protected val _tracks = Collections.synchronizedList(ArrayList<Track>()) // The list of Tracks
    internal val tracks: MutableList<Track>
        get() = _tracks

    protected val _file: File?
        get() {
            if (_tracks.isEmpty()) return null
            return try {
                File("${gpxs(this)}/${name(_tracks.first().time)}.gpx")
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    private var _no = ""
    internal var no: String
        get() = this._no
        set(no) {
            this._no = no
        }

    internal fun pee() {
        //Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val track = lastLocation?.let { Track(it, no = no, event = Track.EVENT.PEE) }
        track?.let { _tracks.add(it) }
        Log.d("TRACK","set:${track}")
        this.write()
    }

    internal fun poo() {
        //Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val track = lastLocation?.let { Track(it, no = no, event = Track.EVENT.POO) }
        track?.let { _tracks.add(it) }
        this.write()
    }

    internal fun mrk() {
        //Log.d(__CLASSNAME__, "${getMethodName()}[$id]${currentLocation.toText()}")
        val track = lastLocation?.let { Track(it, no = no, event = Track.EVENT.MRK) }
        track?.let { _tracks.add(it) }
        this.write()
    }

    internal fun deleteMark(track: Track){
        _tracks.find { trackToRemove ->
            trackToRemove.location.latitude == track.latitude &&
                    trackToRemove.location.longitude == track.longitude &&
                    trackToRemove.no == track.no &&
                    trackToRemove.event == track.event &&
                    trackToRemove.time == track.time
        }?.let { foundTrack ->
            _tracks.remove(foundTrack)
        }

        G.trackChange = true

        this.delete()
    }

    private var _start = 0L
    val start
        get() = (_start > 0L) && _tracks.isNotEmpty()

    override fun title(): String {
        return "${getString(R.string.walk_title_walking)} - ${__duration}"
    }

    val __duration: String
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
        get() = _tracks._duration()

    val duration: String
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
        get() = _tracks._distance()

    val distance: String
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
