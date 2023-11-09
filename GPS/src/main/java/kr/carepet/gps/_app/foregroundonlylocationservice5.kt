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
 *  isyuun@care-pet.kr             2023. 10. 18.   description...
 */

package kr.carepet.gps._app

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationResult
import kr.carepet.gps.R
import kr.carepet.gpx.Track
import kr.carepet.util.Log
import kr.carepet.util.getMethodName
import java.util.Collections

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice4.kt
 * @Date        : 2023. 10. 18.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class foregroundonlylocationservice5 : foregroundonlylocationservice4() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    internal var launchActivityIntent: Intent? = null
    override fun launchActivityIntent(): Intent? {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${super.launchActivityIntent()}][$launchActivityIntent]")
        return launchActivityIntent ?: super.launchActivityIntent()
    }

    override fun generateNotification(location: Location?): Notification {
        generateNotificationChannel(NotificationManager.IMPORTANCE_MAX)
        val title = "${getString(R.string.walk_title_walking)} - ${__duration}"
        val text = "${getString(R.string.app_name)}이 ${getString(R.string.walk_text_in_tracking)}"
        val activityPendingIntent = PendingIntent.getActivity(this, 0, launchActivityIntent(), PendingIntent.FLAG_MUTABLE)
        val ret = notificationCompatBuilder
            //.setStyle(style)      //ㅆㅂ
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.ic_launch,
                getString(R.string.open),
                activityPendingIntent
            )
            //.addAction(
            //    R.drawable.ic_cancel,
            //    getString(R.string.stop),
            //    servicePendingIntent
            //)
            .setContentIntent(activityPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .build()
        Log.wtf(__CLASSNAME__, "${getMethodName()}${location.toText()}, $ret")
        return ret
    }

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