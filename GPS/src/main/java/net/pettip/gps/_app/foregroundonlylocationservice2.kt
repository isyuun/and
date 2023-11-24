package net.pettip.gps._app

import com.google.android.gms.location.LocationResult
import net.pettip.util.Log
import net.pettip.util.getMethodName

open class foregroundonlylocationservice2 : foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun onLocationResult(locationResult: LocationResult) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$locationResult")
        super.onLocationResult(locationResult)
    }
}