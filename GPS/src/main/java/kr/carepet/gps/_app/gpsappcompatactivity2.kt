package kr.carepet.gps._app

import android.content.Intent
import kr.carepet.gps.app.GPSApplication

open class gpsappcompatactivity2 : gpsappcompatactivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    private val application: GPSApplication = GPSApplication.instance

    private fun intent(): Intent {
        val intent = Intent(this, this::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        application.launchActivityIntent = intent
        return intent
    }

    override fun onPause() {
        intent()
        super.onPause()
    }

    override fun onStop() {
        intent()
        super.onStop()
    }
}