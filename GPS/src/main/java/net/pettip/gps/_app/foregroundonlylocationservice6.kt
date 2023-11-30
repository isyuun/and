package net.pettip.gps._app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import net.pettip.gps.R
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.util.Timer
import java.util.TimerTask

open class foregroundonlylocationservice6 : foregroundonlylocationservice5() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private var timer: Timer? = null
    private var cancel = true
    private fun timer() {
        if (!serviceRunningInForeground) return
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (cancel) return
                val title = "${getString(R.string.walk_title_walking)} - ${__duration}"
                //Log.wtf(__CLASSNAME__, "${getMethodName()} $title")
                //notificationManager.notify(NOTIFICATION_ID, notificationCompatBuilder.setContentTitle(title).build())
                with(NotificationManagerCompat.from(this@foregroundonlylocationservice6)) {
                    // notificationId is a unique int for each notification that you must define
                    if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                    notify(NOTIFICATION_ID, notificationCompatBuilder.setContentTitle(title).build())
                }
            }
        }, 1000, 1000) // 1초마다 실행
        cancel = false
    }

    private fun cancel() {
        with(NotificationManagerCompat.from(this)) {
            cancel(NOTIFICATION_ID)
            cancelAll()
        }
        cancel = true
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    //private var notification: Notification? = null
    private fun startForeground() {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        cancel()
        generateNotification(lastLocation)?.let {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                it,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                } else 0
            )
            serviceRunningInForeground = true
            //notificationManager.notify(NOTIFICATION_ID, it)
            with(NotificationManagerCompat.from(this@foregroundonlylocationservice6)) {
                // notificationId is a unique int for each notification that you must define
                if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                notify(NOTIFICATION_ID, it)
            }
            timer()
        }
    }

    private fun stopForeground() {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        cancel()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        val ret = super.onBind(intent)
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        return ret
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        super.onRebind(intent)
    }

    override fun start() {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        startForeground()
        super.start()
    }

    override fun stop() {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        stopForeground()
        super.stop()
    }
}