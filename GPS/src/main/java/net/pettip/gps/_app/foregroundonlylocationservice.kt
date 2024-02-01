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

/**import net.pettip.util.__CLASSNAME__*/
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import net.pettip.util.Log
import net.pettip.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class foregroundonlylocationservice : _foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private var changing = false

    protected var running = false

    private val localBinder = LocalBinder()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    internal var lastLocation: Location? = null

    /**
     * Define a LiveData to observe in activity
     * https://stackoverflow.com/questions/74264850/localbroadcastmanager-is-now-deprecated-how-to-send-data-from-service-to-activi
     */
    private val tokenLiveData = MutableLiveData<String>()

    protected lateinit var notificationCompatBuilder: NotificationCompat.Builder

    override fun onCreate() {
        Log.v(__CLASSNAME__, "${getMethodName()}...")
        notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, GPS_UPDATE_MIllIS).apply {
            setMinUpdateDistanceMeters(GPS_UPDATE_MIN_METERS)
            setWaitForAccurateLocation(true)
        }.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                this@foregroundonlylocationservice.onLocationResult(locationResult)
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                super.onLocationAvailability(locationAvailability)
                this@foregroundonlylocationservice.onLocationAvailability(locationAvailability)
            }

        }
        //Log.wtf(__CLASSNAME__, "${getMethodName()}$fusedLocationProviderClient")
    }

    protected open fun onLocationResult(locationResult: LocationResult) {
        Log.v(__CLASSNAME__, "${getMethodName()}[$locationResult]")
        lastLocation = locationResult.lastLocation
        val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, lastLocation)
        sendBroadcast(intent)
    }

    protected open fun onLocationAvailability(locationAvailability: LocationAvailability) {
    }

    override fun sendBroadcast(intent: Intent) {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[$intent]")
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.v(__CLASSNAME__, "${getMethodName()}...")
        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)
        Log.w(__CLASSNAME__, "${getMethodName()}$cancelLocationTrackingFromNotification, $intent, $flags, $startId")     //Log.d(__CLASSNAME__, "onStartCommand()")
        if (cancelLocationTrackingFromNotification) {
            stop()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.v(__CLASSNAME__, "${getMethodName()}...")
        changing = false
        Log.w(__CLASSNAME__, "${getMethodName()}, $localBinder")
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        Log.v(__CLASSNAME__, "${getMethodName()}...")
        changing = false
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.v(__CLASSNAME__, "${getMethodName()}...")
        // Ensures onRebind() is called if MainActivity (client) rebinds.
        return true
    }

    override fun onDestroy() {
        Log.v(__CLASSNAME__, "${getMethodName()}...")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changing = true
    }

    /**
     * IY:상속시 실행지점을 최종 상속 클래스로 이동한다.
     *
     */
    internal open fun start() {
        Log.i(__CLASSNAME__, "${getMethodName()}${lastLocation.toText()}, $lastLocation")
        //SharedPreferenceUtil.saveLocationTrackingPref(this, true)
        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        val intent = Intent(applicationContext, this::class.java)
        try {
            Log.wtf(__CLASSNAME__, "::startForegroundService${getMethodName()}[$applicationContext][${this::class.java}][$intent]")
            startService(intent)
            ContextCompat.startForegroundService(applicationContext, intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            //SharedPreferenceUtil.saveLocationTrackingPref(this, false)
            Log.e(__CLASSNAME__, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    internal open fun stop() {
        Log.i(__CLASSNAME__, "${getMethodName()}${lastLocation.toText()}, $lastLocation")
        try {
            // TODO: Step 1.6, Unsubscribe to location changes.
            val removeTask = fusedLocationClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(__CLASSNAME__, "Location Callback removed.")
                    stopSelf()
                } else {
                    Log.d(__CLASSNAME__, "Failed to remove Location Callback.")
                }
            }
            //SharedPreferenceUtil.saveLocationTrackingPref(this, false)
        } catch (unlikely: SecurityException) {
            //SharedPreferenceUtil.saveLocationTrackingPref(this, true)
            Log.e(__CLASSNAME__, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
        //stopSelf()
    }

    private fun cancelIntent(): Intent {
        val intent = Intent(this, this::class.java)
        Log.i(__CLASSNAME__, "${getMethodName()}$EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, $intent, $this, ${this::class.java}")
        return intent
    }

    protected open fun launchActivityIntent(): Intent? {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        return intent
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: foregroundonlylocationservice
            get() = this@foregroundonlylocationservice
    }
}