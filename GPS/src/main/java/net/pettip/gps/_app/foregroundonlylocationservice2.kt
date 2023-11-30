/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 10. 18.   description...
 */

package net.pettip.gps._app

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import net.pettip.gps.R
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.util.Timer
import java.util.TimerTask

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice4.kt
 * @Date        : 2023. 10. 18.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class foregroundonlylocationservice2 : foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    internal var launchActivityIntent: Intent? = null
    override fun launchActivityIntent(): Intent? {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${super.launchActivityIntent()}][$launchActivityIntent]")
        return launchActivityIntent ?: super.launchActivityIntent()
    }

    private fun generateNotificationChannel(importance: Int) {
        val title = getString(R.string.app_name)
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, title, importance
        )
        with(NotificationManagerCompat.from(this@foregroundonlylocationservice2)) {
            createNotificationChannel(notificationChannel)
        }
    }

    open fun title(): String {
        return ""
    }

    private var built = false
    private fun generateNotification(location: Location?): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            generateNotificationChannel(NotificationManager.IMPORTANCE_MAX)
        val text = "${getString(R.string.app_name)}이 ${getString(R.string.walk_text_in_tracking)}"
        val activityPendingIntent = PendingIntent.getActivity(this, 0, launchActivityIntent(), PendingIntent.FLAG_MUTABLE)
        val ret = if (built) notificationCompatBuilder.build() else
            notificationCompatBuilder
                //.setStyle(style)      //ㅆㅂ
                .setContentTitle(title())
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
        built = true
        return ret
    }

    private var timer: Timer? = null
    private var cancel = true
    private fun timer() {
        if (!running) return
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (cancel) return
                with(NotificationManagerCompat.from(this@foregroundonlylocationservice2)) {
                    // notificationId is a unique int for each notification that you must define
                    if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                    notify(NOTIFICATION_ID, notificationCompatBuilder.setContentTitle(title()).build())
                }
            }
        }, 1000, 1000) // 1초마다 실행
        cancel = false
    }

    private fun cancel() {
        cancel = true
        with(NotificationManagerCompat.from(this)) {
            cancel(NOTIFICATION_ID)
            cancelAll()
        }
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    override fun start() {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        if (running) cancel()
        running = true
        val notification = generateNotification(lastLocation)
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION else 0
        )
        with(NotificationManagerCompat.from(this@foregroundonlylocationservice2)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(NOTIFICATION_ID, notification)
        }
        timer()
        super.start()
    }

    override fun stop() {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        running = false
        cancel()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        super.stop()
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        return super.onBind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        super.onRebind(intent)
    }
}