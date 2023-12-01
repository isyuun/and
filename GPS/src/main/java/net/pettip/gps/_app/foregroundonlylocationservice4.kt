package net.pettip.gps._app

import android.location.Location
import com.google.android.gms.location.LocationResult
import net.pettip.gpx.Track
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.util.Collections

open class foregroundonlylocationservice4 : foregroundonlylocationservice3() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onLocationResult(locationResult: LocationResult) {
        Log.i(__CLASSNAME__, "${getMethodName()}[${(start && pause)}][start:$start][pause:$pause]$_pauses")
        if (start && pause) {
            val exit = exit(locationResult)
            Log.wtf(__CLASSNAME__, "${getMethodName()}[exit:$exit]$lastLocation$locationResult")
            lastLocation = locationResult.lastLocation
            if (exit) return
            lastLocation?.let { _pauses.add(Track(it)) }
            Log.i(__CLASSNAME__, "${getMethodName()}[${(start && pause)}][start:$start][pause:$pause]$_pauses")
        } else {
            super.onLocationResult(locationResult)
        }
    }

    internal var pause = false
    private var _pause: Track? = null
    private val _pauses = Collections.synchronizedList(ArrayList<Track>()) // The list of Tracks

    fun pause() {
        //val loc = location    //ㅆㅂ
        val loc = lastLocation?.let { Location(it) }
        Log.wtf(__CLASSNAME__, "${getMethodName()}::write[${(start && pause)}][start:$start][pause:$pause][${lastLocation == loc}][${tracks.size}]")
        if (!start or pause) return else pause = true
        loc?.let {
            it.time = System.currentTimeMillis()
            _pause = Track(it)
            if (!_tracks.contains(_pause)) _tracks.add(_pause)
        }
        write()
        _pauses.clear()
    }

    fun resume() {
        //val loc = location    //ㅆㅂ
        val loc = lastLocation?.let { Location(it) }
        Log.wtf(__CLASSNAME__, "${getMethodName()}::write[${(start && pause)}][start:$start][pause:$pause][${lastLocation == loc}][${tracks.size}]")
        if (!start or !pause) return else pause = false
        if (_tracks.contains(_pause)) _tracks.remove(_pause)
        if (_pauses.isNotEmpty()) _tracks.addAll(_pauses)
        write()
        _pauses.clear()
    }

    override fun start() {
        val loc = lastLocation?.let { Location(it) }
        Log.wtf(__CLASSNAME__, "${getMethodName()}::write[${(start && pause)}][start:$start][pause:$pause][${lastLocation == loc}][${tracks.size}]")
        super.start()
        pause = false
        _pauses.clear()
    }

    override fun stop() {
        val loc = lastLocation?.let { Location(it) }
        Log.wtf(__CLASSNAME__, "${getMethodName()}::write[${(start && pause)}][start:$start][pause:$pause][${lastLocation == loc}][${tracks.size}]")
        super.stop()
        pause = false
        _pauses.clear()
    }
}