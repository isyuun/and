package kr.carepet.gps._app

import android.content.Intent
import kr.carepet.gps.app.GPSApplication
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

open class gpsappcompatactivity2 : gpsappcompatactivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    private val application: GPSApplication = GPSApplication.instance

    override fun onPause() {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
        application.launchActivityIntent = intent
        super.onPause()
    }

    fun main() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)/*Intent(this, MainActivity::class.java)*/
        intent?.action = Intent.ACTION_MAIN
        intent?.addCategory(Intent.CATEGORY_LAUNCHER)
        intent?.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
    }

    override fun onBackPressed() {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
        super.onBackPressed()
        post { main() }
    }

    override fun finish() {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
        super.finish()
        post { main() }
    }
}