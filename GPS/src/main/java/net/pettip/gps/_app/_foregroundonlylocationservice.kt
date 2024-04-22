/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 20.   description...
 */

package net.pettip.gps._app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import net.pettip.app.Service
import net.pettip.util.Log
import net.pettip.util.getMethodName

const val GPS_MAX_SATELLITES = 18
const val GPS_RELOAD_MINUTES = 10L
const val GPS_UPDATE_MIllIS = 1L
const val GPS_UPDATE_MIN_METERS = 5.0f
const val GPS_UPDATE_MAX_METERS = 15.0f
const val GPS_LATITUDE_ZERO = 37.546855      //37.5
const val GPS_LONGITUDE_ZERO = 127.065330    //127.0
const val GPS_CAMERA_ZOOM_ZERO = 17.0

private const val PACKAGE_NAME = "net.pettip.app.navi"

internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
    "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

internal const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
    "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"

internal const val NOTIFICATION_ID = 12345678

internal const val NOTIFICATION_CHANNEL_ID = "net.pettip.app.navi.channel.01"

internal const val SHARED_PREFERENCE_FILE_KEY = "$PACKAGE_NAME.PREFERENCE_FILE_KEY"

internal const val KEY_FOREGROUND_ENABLED = "key.foreground.enabled"
internal const val KEY_FOREGROUND_GPXFILE = "key.foreground.gpxflie"
internal const val KEY_FOREGROUND_GPXPETS = "key.foreground.gpxpets"

/**
 * Provides access to SharedPreferences for location to Activities and Services.
 */
private object SharedPreferenceUtil {

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The [Context].
     */
    fun getLocationTrackingPref(context: Context): Boolean =
        context.getSharedPreferences(
            SHARED_PREFERENCE_FILE_KEY,
            Context.MODE_PRIVATE
        ).getBoolean(KEY_FOREGROUND_ENABLED, false)

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) =
        context.getSharedPreferences(
            SHARED_PREFERENCE_FILE_KEY,
            Context.MODE_PRIVATE
        ).edit {
            putBoolean(KEY_FOREGROUND_ENABLED, requestingLocationUpdates)
        }
}


/**
 * @Project     : carepet-android
 * @FileName    : _foregroundonlylocationservice.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class _foregroundonlylocationservice : Service() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private lateinit var locationManager: LocationManager
    private lateinit var gnssStatusCallback: GnssStatus.Callback
    private lateinit var gnssStatus: GnssStatus
    private var numSatsTotal: Int = -1
    private var numSatsCount: Int = -1

    protected fun registerGnssStatusCallback() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager // Location Manager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        gnssStatusCallback = object : GnssStatus.Callback() {
            override fun onStarted() {
                Log.i(__CLASSNAME__, "${getMethodName()}[$this]")
                super.onStarted()
            }

            override fun onStopped() {
                Log.i(__CLASSNAME__, "${getMethodName()}[$this]")
                super.onStopped()
            }

            override fun onFirstFix(ttffMillis: Int) {
                Log.i(__CLASSNAME__, "${getMethodName()}[$this]")
                super.onFirstFix(ttffMillis)
            }

            override fun onSatelliteStatusChanged(status: GnssStatus) {
                //Log.i(__CLASSNAME__, "${getMethodName()}[$this][$status]")
                super.onSatelliteStatusChanged(status)
                numSatsTotal = status.satelliteCount // 현재 사용 가능한 위성수
                numSatsCount = 0 // usedIn
                for (i in 0 until numSatsTotal) {
                    if (status.usedInFix(i)) numSatsCount++
                }
                Log.v(__CLASSNAME__, "${getMethodName()}[gps:${gps()}][sat:${sat()}][sta:$status]")
            }
        }
        locationManager.registerGnssStatusCallback(gnssStatusCallback, Handler(Looper.getMainLooper()))
    }

    protected fun unregisterGnssStatusCallback() {
        locationManager.unregisterGnssStatusCallback(gnssStatusCallback)
    }

    internal fun sat(): String {
        return "$numSatsCount/$numSatsTotal"
    }

    internal fun gps(): Boolean {
        return (numSatsCount > GPS_MAX_SATELLITES)
    }

    internal fun moc(loc: Location?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            loc?.isMock == true
        } else {
            loc?.isFromMockProvider == true
        }
    }

    protected open fun init() {}

    protected open fun unit() {}

    override fun onCreate() {
        Log.i(__CLASSNAME__, "${getMethodName()}[$this]")
        super.onCreate()
        registerGnssStatusCallback()
    }

    override fun onDestroy() {
        Log.i(__CLASSNAME__, "${getMethodName()}[$this]")
        super.onDestroy()
        unregisterGnssStatusCallback()
    }
}