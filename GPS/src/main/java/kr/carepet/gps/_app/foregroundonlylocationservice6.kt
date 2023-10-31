package kr.carepet.gps._app

import android.app.Notification
import android.content.Intent
import android.os.IBinder
import kr.carepet.gps.R
import kr.carepet.util.Log
import kr.carepet.util.getMethodName
import java.util.Timer
import java.util.TimerTask

open class foregroundonlylocationservice6 : foregroundonlylocationservice5() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private lateinit var timer: Timer
    private fun timer() {
        if (!serviceRunningInForeground) return
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val title = "${getString(R.string.walk_title_walking)} - ${duration}"
                //Log.wtf(__CLASSNAME__, "${getMethodName()} $title")
                notification = notificationCompatBuilder.setContentTitle(title).build()
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        }, 1000, 1000) // 1초마다 실행
    }

    private var notification: Notification? = null
    private fun startForeground() {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        if (notification == null) notification = generateNotification(lastLocation)
        startForeground(NOTIFICATION_ID, notification)
        serviceRunningInForeground = true
        notificationManager.notify(NOTIFICATION_ID, notification)
        timer()
    }

    private fun stopForeground() {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        stopForeground(STOP_FOREGROUND_REMOVE)      //stopForeground(true)
        timer.cancel()
        timer.purge()
        notification = null
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        return super.onBind(intent)
    }

    //override fun onUnbind(intent: Intent): Boolean {
    //    Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
    //    startForeground()
    //    return super.onUnbind(intent)
    //}
    //
    //override fun onRebind(intent: Intent) {
    //    Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
    //    super.onRebind(intent)
    //    stopForeground()
    //}

    override fun start() {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        startForeground()
        super.start()
    }

    override fun stop() {
        Log.i(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        super.stop()
        stopForeground()
    }
}