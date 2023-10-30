package kr.carepet.gps._app

import android.content.Intent
import kr.carepet.gps.app.GPSApplication
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

open class gpsappcompatactivity2 : gpsappcompatactivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    private val application: GPSApplication = GPSApplication.instance

    override fun onPause() {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent,${intent?.flags}")
        application.launchActivityIntent = intent
        super.onPause()
    }
}