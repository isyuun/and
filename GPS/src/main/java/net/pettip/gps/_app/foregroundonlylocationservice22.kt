package net.pettip.gps._app

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import net.pettip.util.Log
import net.pettip.util.getMethodName

open class foregroundonlylocationservice22 : foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    /**
     * Define a LiveData to observe in activity
     * https://stackoverflow.com/questions/74264850/localbroadcastmanager-is-now-deprecated-how-to-send-data-from-service-to-activi
     */
    private val tokenLiveData = MutableLiveData<String>()

    override fun sendBroadcast(intent: Intent) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$intent")
        super.sendBroadcast(intent)
    }
}