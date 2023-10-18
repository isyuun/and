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
 *  isyuun@care-pet.kr             2023. 10. 18.   description...
 */

package kr.carepet.gps._app

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import kr.carepet.gps.R
import kr.carepet.gps.app.GPSApplication
import kr.carepet.util.Log
import kr.carepet.util.getMethodName
import java.util.Timer
import java.util.TimerTask

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice4.kt
 * @Date        : 2023. 10. 18.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class foregroundonlylocationservice4 : foregroundonlylocationservice3() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun generateNotification(location: android.location.Location?): Notification {
        //var ret = super.generateNotification(location)
        val title = "${getString(R.string.walk_title_walking)} - ${_duration}"
        val text = "${getString(R.string.app_name)}이 ${getString(R.string.walk_text_in_tracking)}"
        //val style = NotificationCompat.BigTextStyle()
        //    .setBigContentTitle(title)
        //    .bigText(text)
        val activityPendingIntent = PendingIntent.getActivity(this, 0, launchActivityIntent(), PendingIntent.FLAG_MUTABLE)
        val ret = notificationCompatBuilder
            //.setStyle(style)
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
            .build()
        Log.wtf(__CLASSNAME__, "${getMethodName()}${location.toText()}, $ret")
        return ret
    }

    private lateinit var timer: Timer
    private fun timer() {
        if (!serviceRunningInForeground) return
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val title = "${getString(R.string.walk_title_walking)} - ${_duration}"
                Log.wtf(__CLASSNAME__, "${getMethodName()} $title")
                notification = notificationCompatBuilder.setContentTitle(title).build()
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        }, 1000, 1000) // 1초마다 실행
    }

    private var notification: Notification? = null
    override fun onUnbind(intent: Intent): Boolean {
        if (!configurationChange && SharedPreferenceUtil.getLocationTrackingPref(this)) {
            if (notification == null) notification = generateNotification(currentLocation)
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
            notificationManager.notify(NOTIFICATION_ID, notification)
            timer()
        }
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {
        try {
            timer.cancel()
            timer.purge()
            timer == null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onRebind(intent)
    }

}