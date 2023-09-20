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
/**import android.icu.text.SimpleDateFormat*/
import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import kr.carepet.app.Application
import kr.carepet.gps.app.ForegroundOnlyLocationService
import kr.carepet.gps.app.ForegroundOnlyLocationService.LocalBinder
import kr.carepet.gps.R
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**import java.util.Date*/

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

/**
 *  This app allows a user to receive location updates without the background permission even when
 *  the app isn't in focus. This is the preferred approach for Android.
 *
 *  It does this by creating a foreground service (tied to a Notification) when the
 *  user navigates away from the app. Because of this, it only needs foreground or "while in use"
 *  location permissions. That is, there is no need to ask for location in the background (which
 *  requires additional permissions in the manifest).
 *
 *  Note: Users have the following options in Android 11+ regarding location:
 *
 *  * Allow all the time
 *  * Allow while app is in use, i.e., while app is in foreground (new in Android 10)
 *  * Allow one time use (new in Android 11)
 *  * Not allow location at all
 *
 * It is generally recommended you only request "while in use" location permissions (location only
 * needed in the foreground), e.g., fine and coarse. If your app has an approved use case for
 * using location in the background, request that permission in context and separately from
 * fine/coarse location requests. In addition, if the user denies the request or only allows
 * "while-in-use", handle it gracefully. To see an example of background location, please review
 * {@link https://github.com/android/location-samples/tree/master/LocationUpdatesBackgroundKotlin}.
 *
 * Android 10 and higher also now requires developers to specify foreground service type in the
 * manifest (in this case, "location").
 *
 * For the feature that requires location in the foreground, this sample uses a long-running bound
 * and started service for location updates. The service is aware of foreground status of this
 * activity, which is the only bound client in this sample.
 *
 * While getting location in the foreground, if the activity ceases to be in the foreground (user
 * navigates away from the app), the service promotes itself to a foreground service and continues
 * receiving location updates.
 *
 * When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that foreground service is removed.
 *
 * While the foreground service notification is displayed, the user has the option to launch the
 * activity from the notification. The user can also remove location updates directly from the
 * notification. This dismisses the notification and stops the service.
 */
/**
 * @Project     : carepet-android
 * @FileName    : gpsapplication.kt
 * @Date        : 2023. 09. 04.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class gpsapplication : Application(), SharedPreferences.OnSharedPreferenceChangeListener, ServiceConnection {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while-in-use feature.
    protected var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    // Listens for location broadcasts from ForegroundOnlyBroadcastReceiver2.
    //@Deprecated("Use foregroundOnlyBroadcastReceiver instead. ", ReplaceWith("foregroundOnlyBroadcastReceiver"))
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver

    private lateinit var sharedPreferences: SharedPreferences

    //private lateinit var foregroundOnlyLocationButton: Button

    //private lateinit var outputTextView: TextView

    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            this@gpsapplication.onServiceConnected(name, service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            this@gpsapplication.onServiceDisconnected(name)
        }
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val binder: LocalBinder = service as LocalBinder
        foregroundOnlyLocationService = binder.service
        foregroundOnlyLocationServiceBound = true
        Log.wtf(__CLASSNAME__, "${getMethodName()}${foregroundOnlyLocationServiceBound},${foregroundOnlyLocationService}")
    }

    override fun onServiceDisconnected(name: ComponentName) {
        foregroundOnlyLocationService = null
        foregroundOnlyLocationServiceBound = false
        Log.wtf(__CLASSNAME__, "${getMethodName()}${foregroundOnlyLocationServiceBound},${foregroundOnlyLocationService}")
    }

    override fun onCreate() {
        Log.d(__CLASSNAME__, "${getMethodName()}...")
        super.onCreate()
        init()
    }

    protected open fun init() {
        Log.d(__CLASSNAME__, "${getMethodName()}...")
        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
        Log.w(__CLASSNAME__, "${getMethodName()}${foregroundOnlyBroadcastReceiver}")
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)
        Log.w(__CLASSNAME__, "${getMethodName()}$sharedPreferences")
    }

    protected fun start() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}${foregroundPermissionApproved()}, $foregroundOnlyLocationService")
        // TODO: Step 1.0, Review Permissions: Checks and requests if needed.
        if (foregroundPermissionApproved()) {
            foregroundOnlyLocationService?.start() ?: Log.w(__CLASSNAME__, "${getMethodName()}Service Not Bound")
        } else {
            requestForegroundPermissions()
        }
    }

    protected fun stop() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}${foregroundPermissionApproved()},${foregroundOnlyLocationService}")
        foregroundOnlyLocationService?.stop()
    }

    internal fun onStart() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$foregroundOnlyLocationServiceBound, $foregroundOnlyServiceConnection")
        //super.onStart()
        updateButtonState(
            sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
        )
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        val serviceIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, BIND_AUTO_CREATE)
    }

    /**
     * @see gpsapplication2.onResume
     *
     * IY: deprecate*/
    internal open fun onResume() {}

    /**
     * @see gpsapplication2.onPause
     *
     * IY: deprecate*/
    internal open fun onPause() {}

    internal fun onStop() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$foregroundOnlyLocationServiceBound, $foregroundOnlyServiceConnection")
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        //super.onStop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.i(__CLASSNAME__, "${getMethodName()}$sharedPreferences,$key")
        // Updates button states if new while in use location is added to SharedPreferences.
        if (key == SharedPreferenceUtil.KEY_FOREGROUND_ENABLED) {
            sharedPreferences?.let {
                updateButtonState(
                    it.getBoolean(
                        SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
                    )
                )
            }
        }
    }

    // TODO: Step 1.0, Review Permissions: Method checks if permissions approved.
    private fun foregroundPermissionApproved(): Boolean {
        val ret = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        Log.w(__CLASSNAME__, "${getMethodName()}$sharedPreferences,$ret")
        return ret
    }

    // TODO: Step 1.0, Review Permissions: Method requests permissions.
    private fun requestForegroundPermissions() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$sharedPreferences")
        //val provideRationale = foregroundPermissionApproved()
        //
        //// If the user denied a previous request, but didn't check "Don't ask again", provide
        //// additional rationale.
        //if (provideRationale) {
        //    Snackbar.make(
        //        findViewById(R.id.activity_main),
        //        R.string.permission_rationale,
        //        Snackbar.LENGTH_LONG
        //    )
        //        .setAction(R.string.ok) {
        //            // Request permission
        //            ActivityCompat.requestPermissions(
        //                this@MainActivity,
        //                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        //                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        //            )
        //        }
        //        .show()
        //} else {
        //    Log.d(__CLASSNAME__, "Request foreground only permission")
        //    ActivityCompat.requestPermissions(
        //        this@MainActivity,
        //        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        //        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        //    )
        //}
    }

    // TODO: Step 1.0, Review Permissions: Handles permission result.
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(__CLASSNAME__, "${getMethodName()}$requestCode, $permissions, $grantResults")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(__CLASSNAME__, "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    foregroundOnlyLocationService?.start()

                else -> {
                    // Permission denied.
                    updateButtonState(false)

                    //Snackbar.make(
                    //    findViewById(R.id.activity_main),
                    //    R.string.permission_denied_explanation,
                    //    Snackbar.LENGTH_LONG
                    //)
                    //.setAction(R.string.settings) {
                    //    // Build intent that displays the App settings screen.
                    //    val intent = Intent()
                    //    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    //    val uri = Uri.fromParts(
                    //        "package",
                    //        BuildConfig.APPLICATION_ID,
                    //        null
                    //    )
                    //    intent.data = uri
                    //    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    //    startActivity(intent)
                    //}
                    //.show()
                }
            }
        }
    }

    private fun updateButtonState(trackingLocation: Boolean) {
        if (trackingLocation) {
            //foregroundOnlyLocationButton.text = getString(R.string.stop_location_updates_button_text)
        } else {
            //foregroundOnlyLocationButton.text = getString(R.string.start_location_updates_button_text)
        }
    }

    protected fun logResultsToScreen(output: String) {
        //Log.wtf(__CLASSNAME__, "${getMethodName()}$output")
        //val outputWithPreviousLogs = "$output\n${outputTextView.text}"
        //outputTextView.text = outputWithPreviousLogs
    }

    /**
     * Receiver for location broadcasts from [ForegroundOnlyLocationService].
     *
     * IY: Deprecated
     *
     * @see : kr.carepet.gps.ForegroundOnlyBroadcastReceiver2
     */
    //@Deprecated("Use kr.carepet.gps.ForegroundOnlyBroadcastReceiver2 instead. ", ReplaceWith("kr.carepet.gps.ForegroundOnlyBroadcastReceiver2"))
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            //Log.wtf(__CLASSNAME__, "${getMethodName()}$context, $intent")
            val location =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(EXTRA_LOCATION, Location::class.java)
                } else {
                    intent.getParcelableExtra(EXTRA_LOCATION)
                }

            if (location != null) {
                //val tick = SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSSZ", resources.configuration.locales[0]).format(Date(System.currentTimeMillis()))
                val tick = ""
                logResultsToScreen("${tick} - ${location.toText()}, $location")
            }
        }
    }
}