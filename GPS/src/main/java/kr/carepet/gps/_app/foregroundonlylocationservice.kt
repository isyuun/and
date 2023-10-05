/*
 *  Copyright 2011 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.gps._app

/**import kr.carepet.util.__CLASSNAME__*/
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kr.carepet.gps.R
import kr.carepet.gpx.GPX_INTERVAL_UPDATE_METERS
import kr.carepet.gpx.GPX_INTERVAL_UPDATE_MIllIS
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : foregroundonlylocationservice.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class foregroundonlylocationservice() : _foregroundonlylocationservice() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    /*
     * Checks whether the bound activity has really gone away (foreground service with notification
     * created) or simply orientation change (no-op).
     */
    private var configurationChange = false

    protected var serviceRunningInForeground = false

    private val localBinder = LocalBinder()

    private lateinit var notificationManager: NotificationManager

    // TODO: Step 1.1, Review variables (no changes).
    // FusedLocationProviderClient - Main class for receiving location updates.
    protected lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e., how often you should receive
    // updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private lateinit var locationCallback: LocationCallback

    // Used only for local storage of the last known location. Usually, this would be saved to your
    // database, but because this is a simplified sample without a full database, we only need the
    // last location to create a Notification if the user navigates away from the app.
    protected var currentLocation: Location? = null

    //https://stackoverflow.com/questions/74264850/localbroadcastmanager-is-now-deprecated-how-to-send-data-from-service-to-activi
    //Define a LiveData to observe in activity
    private val tokenLiveData = MutableLiveData<String>()

    override fun onCreate() {
        Log.d(__CLASSNAME__, "${getMethodName()}[$serviceRunningInForeground]")        //Log.d(__CLASSNAME__, "onCreate()")

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // TODO: Step 1.2, Review the FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // TODO: Step 1.3, Create a LocationRequest.
        /** This method is deprecated. Use LocationRequest.Builder instead. May be removed in a future release */
        //locationRequest = LocationRequest.create().apply {
        //    // Sets the desired interval for active location updates. This interval is inexact. You
        //    // may not receive updates at all if no location sources are available, or you may
        //    // receive them less frequently than requested. You may also receive updates more
        //    // frequently than requested if other applications are requesting location at a more
        //    // frequent interval.
        //    //
        //    // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
        //    // targetSdkVersion) may receive updates less frequently than this interval when the app
        //    // is no longer in the foreground.
        //    interval = TimeUnit.SECONDS.toMillis(60)
        //
        //    // Sets the fastest rate for active location updates. This interval is exact, and your
        //    // application will never receive updates more frequently than this value.
        //    fastestInterval = TimeUnit.SECONDS.toMillis(30)
        //
        //    // Sets the maximum time when batched location updates are delivered. Updates may be
        //    // delivered sooner than this interval.
        //    maxWaitTime = TimeUnit.MINUTES.toMillis(2)
        //
        //    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        //}
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, GPX_INTERVAL_UPDATE_MIllIS).apply {
            setMinUpdateDistanceMeters(GPX_INTERVAL_UPDATE_METERS)
            //setIntervalMillis(100)
            //setMinUpdateIntervalMillis(50)
            //setGranularity(Granularity.GRANULARITY_FINE)
            //setWaitForAccurateLocation(true)
        }.build()

        // TODO: Step 1.4, Initialize the LocationCallback.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                this@foregroundonlylocationservice.onLocationResult(locationResult)
            }
        }
        //Log.wtf(__CLASSNAME__, "${getMethodName()}$fusedLocationProviderClient")
    }

    private fun actionForegroundIntent(): Intent {
        val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, currentLocation)
        return intent
    }

    protected open fun onLocationResult(locationResult: LocationResult) {
        //Log.d(__CLASSNAME__, "${getMethodName()}[$serviceRunningInForeground], $locationResult")
        // Normally, you want to save a new location to a database. We are simplifying
        // things a bit and just saving it as a local variable, as we only need it again
        // if a Notification is created (when the user navigates away from app).
        currentLocation = locationResult.lastLocation

        // Notify our Activity that a new location was added. Again, if this was a
        // production app, the Activity would be listening for changes to a database
        // with new locations, but we are simplifying things a bit to focus on just
        // learning the location side of things.
        //val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        //intent.putExtra(EXTRA_LOCATION, currentLocation)
        val intent = actionForegroundIntent()
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        // Updates notification content if this service is running as a foreground
        // service.
        //Log.wtf(__CLASSNAME__, "${getMethodName()}${location.toText()}, $location, $locationResult")
        if (serviceRunningInForeground) {
            val notification = generateNotification(currentLocation)
            notificationManager.notify(
                NOTIFICATION_ID,
                notification
            )
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)
        Log.w(__CLASSNAME__, "${getMethodName()}$cancelLocationTrackingFromNotification, $intent, $flags, $startId")     //Log.d(__CLASSNAME__, "onStartCommand()")
        if (cancelLocationTrackingFromNotification) {
            stop()
        }
        // Tells the system not to recreate the service after it's been killed.
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        // MainActivity (client) comes into foreground and binds to service, so the service can
        // become a background services.
        stopForeground(STOP_FOREGROUND_REMOVE)      //stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        Log.w(__CLASSNAME__, "${getMethodName()}[$serviceRunningInForeground], $localBinder")
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        Log.d(__CLASSNAME__, "onRebind()")

        // MainActivity (client) returns to the foreground and rebinds to service, so the service
        // can become a background services.
        stopForeground(STOP_FOREGROUND_REMOVE)      //stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        Log.w(__CLASSNAME__, "${getMethodName()}[$serviceRunningInForeground]")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        //Log.d(__CLASSNAME__, "onUnbind()")

        //Log.i(__CLASSNAME__, "${getMethodName()}:${(!configurationChange && SharedPreferenceUtil.getLocationTrackingPref(this))}")
        // MainActivity (client) leaves foreground, so service needs to become a foreground service
        // to maintain the 'while-in-use' label.
        // NOTE: If this method is called due to a configuration change in MainActivity,
        // we do nothing.
        if (!configurationChange && SharedPreferenceUtil.getLocationTrackingPref(this)) {
            //Log.d(__CLASSNAME__, "Start foreground service")
            val notification = generateNotification(currentLocation)
            //Log.w(__CLASSNAME__, "${getMethodName()}$notification")
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
        }

        // Ensures onRebind() is called if MainActivity (client) rebinds.
        return true
    }

    override fun onDestroy() {
        Log.d(__CLASSNAME__, "onDestroy()")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    /**
     * IY:상속시 실행지점을 최종 상속 클래스로 이동한다.
     *
     */
    private fun subscribeToLocationUpdates() {
        Log.w(__CLASSNAME__, "${getMethodName()}[$serviceRunningInForeground]")        //Log.d(__CLASSNAME__, "subscribeToLocationUpdates()")

        SharedPreferenceUtil.saveLocationTrackingPref(this, true)

        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        Log.wtf(__CLASSNAME__, "${getMethodName()}${applicationContext}, ${this::class.java}")
        startService(Intent(applicationContext, this::class.java))

        try {
            // TODO: Step 1.5, Subscribe to location changes.
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
            Log.e(__CLASSNAME__, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    private fun unsubscribeToLocationUpdates() {
        Log.w(__CLASSNAME__, "${getMethodName()}[$serviceRunningInForeground]")        //Log.d(__CLASSNAME__, "unsubscribeToLocationUpdates()")

        try {
            // TODO: Step 1.6, Unsubscribe to location changes.
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(__CLASSNAME__, "Location Callback removed.")
                    stopSelf()
                } else {
                    Log.d(__CLASSNAME__, "Failed to remove Location Callback.")
                }
            }
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, true)
            Log.e(__CLASSNAME__, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    private fun cancelIntent(): Intent {
        val intent: Intent = Intent(this, this::class.java)
        //Log.i(__CLASSNAME__, "${getMethodName()}$EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, $intent, $this, ${this::class.java}")
        return intent
    }

    private fun launchActivityIntent(): Intent? {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        //Log.i(__CLASSNAME__, "${getMethodName()}$EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, $intent, $this, ${this::class.java}")
        return intent
    }

    protected lateinit var notificationCompatBuilder: NotificationCompat.Builder

    /*
     * Generates a BIG_TEXT_STYLE Notification that represent latest location.
     */
    protected open fun generateNotification(location: Location?): Notification? {

        // Main steps for building a BIG_TEXT_STYLE notification:
        //      0. Get data
        //      1. Create Notification Channel for O+
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up Intent / Pending Intent for notification
        //      4. Build and issue the notification

        // 0. Get data
        val mainNotificationText = location?.toText() ?: getString(R.string.no_location_text)
        val titleText = getString(R.string.app_name)

        // 1. Create Notification Channel for O+ and beyond devices (26+).

        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT
        )

        // Adds NotificationChannel to system. Attempting to create an
        // existing notification channel with its original values performs
        // no operation, so it's safe to perform the below sequence.
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //
        //    val notificationChannel = NotificationChannel(
        //        NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT
        //    )
        //
        //    // Adds NotificationChannel to system. Attempting to create an
        //    // existing notification channel with its original values performs
        //    // no operation, so it's safe to perform the below sequence.
        //    notificationManager.createNotificationChannel(notificationChannel)
        //}
        notificationManager.createNotificationChannel(notificationChannel)

        // 2. Build the BIG_TEXT_STYLE.
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)

        // 3. Set up main Intent/Pending Intents for notification.
        val cancelIntent = cancelIntent()
        cancelIntent.putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)
        //Log.wtf(__CLASSNAME__, "${getMethodName()}$cancelIntent, $EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION")
        val servicePendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val launchActivityIntent = launchActivityIntent()
        //Log.wtf(__CLASSNAME__, "${getMethodName()}$launchActivityIntent")
        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivityIntent, PendingIntent.FLAG_MUTABLE
        )

        // 4. Build and issue the notification.
        // Notification Channel Id is ignored for Android pre O (26).
        notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
        val ret = notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.ic_launch,
                getString(R.string.start),    //getString(R.string.launch_activity),
                activityPendingIntent
            )
            .addAction(
                R.drawable.ic_cancel,
                getString(R.string.stop),  //getString(R.string.stop_location_updates_button_text),
                servicePendingIntent
            )
            .build()
        //Log.i(__CLASSNAME__, "${getMethodName()}$ret, ${this.notificationCompatBuilder}")
        return ret
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: foregroundonlylocationservice
            get() = this@foregroundonlylocationservice
    }

    //companion object {
    //    private const val TAG = "foregroundonlylocationservice"
    //
    //    private const val PACKAGE_NAME = "com.example.android.whileinuselocation"
    //
    //    internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
    //        "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"
    //
    //    internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"
    //
    //    private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
    //        "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"
    //
    //    private const val NOTIFICATION_ID = 12345678
    //
    //    private const val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_01"
    //}

    internal open fun start() {
        //Log.i(__CLASSNAME__, "${getMethodName()}${currentLocation.toText()}, $currentLocation")
        subscribeToLocationUpdates()
    }

    internal open fun stop() {
        //Log.i(__CLASSNAME__, "${getMethodName()}${currentLocation.toText()}, $currentLocation")
        unsubscribeToLocationUpdates()
        stopSelf()
    }

}