package net.pettip.gps._app

import android.content.Intent
import net.pettip.util.Log
import net.pettip.util.getMethodName

open class foregroundonlylocationservice22 : foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun sendBroadcast(intent: Intent) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$intent")
        super.sendBroadcast(intent)
    }
}