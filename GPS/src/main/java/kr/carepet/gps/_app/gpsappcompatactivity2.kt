package kr.carepet.gps._app

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

    //override fun onBackPressed() {
    //    super.onBackPressed()
    //    val intent = packageManager.getLaunchIntentForPackage(packageName)/*Intent(this, MainActivity::class.java)*/
    //    intent?.action = Intent.ACTION_MAIN
    //    intent?.addCategory(Intent.CATEGORY_LAUNCHER)
    //    intent?.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    //    startActivity(intent)
    //    Log.wtf(__CLASSNAME__, "${getMethodName()}[$intent]")
    //}
}