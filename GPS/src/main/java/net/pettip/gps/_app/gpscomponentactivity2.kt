package net.pettip.gps._app

import android.content.Intent
import net.pettip.gps.app.GPSApplication
import net.pettip.singleton.G
import net.pettip.util.Log
import net.pettip.util.getMethodName

open class gpscomponentactivity2 : gpscomponentactivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    private val application: GPSApplication = GPSApplication.instance

    var canBack = false

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

        if (!G.showCameraX){
            post { main() }
        }else{
            Log.d("BACK","backPressTrue")
        }
    }

    override fun finish() {
        Log.i(__CLASSNAME__, "${getMethodName()}$intent")
        super.finish()
        post { main() }
    }
}