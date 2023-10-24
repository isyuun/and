package kr.carepet.gps._app

import com.google.android.gms.location.LocationResult
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

class foregroundonlylocationservice2 : foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun onLocationResult(locationResult: LocationResult) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$locationResult")
        super.onLocationResult(locationResult)
    }
}