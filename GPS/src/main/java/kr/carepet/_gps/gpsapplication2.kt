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
 *  isyuun@care-pet.kr             2023. 9. 7.   description...
 */

package kr.carepet._gps

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kr.carepet.gps.ForegroundOnlyBroadcastReceiver2
import kr.carepet.gps.ForegroundOnlyLocationService
import kr.carepet.gps.IForegroundOnlyBroadcastReceiver
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : gpsapplication2.kt
 * @Date        : 2023. 09. 07.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class gpsapplication2 : gpsapplication(), IForegroundOnlyBroadcastReceiver {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceConnected(name, service)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceDisconnected(name)
    }

    override fun start() {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.start()
    }

    override fun stop() {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.stop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.i(__CLASSNAME__, "${getMethodName()}$sharedPreferences, $key")
        super.onSharedPreferenceChanged(sharedPreferences, key)
    }

    internal fun registerReceiver2(receiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver,
            IntentFilter(
                ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
            )
        )
    }

    internal fun unregisterReceiver2(receiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            receiver
        )
    }

    internal fun registerReceiver() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$foregroundOnlyBroadcastReceiver")
        registerReceiver2(foregroundOnlyBroadcastReceiver)
    }

    internal fun unregisterReceiver() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$foregroundOnlyBroadcastReceiver")
        unregisterReceiver2(foregroundOnlyBroadcastReceiver)
    }

    // Listens for location broadcasts from ForegroundOnlyBroadcastReceiver2.
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver2

    override fun init() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.init()
        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver2(this)
        Log.w(__CLASSNAME__, "${getMethodName()}$foregroundOnlyBroadcastReceiver")
    }

    internal fun location4Intent(intent: Intent): Location? {
        val location =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(ForegroundOnlyLocationService.EXTRA_LOCATION, Location::class.java)
            } else {
                intent.getParcelableExtra(ForegroundOnlyLocationService.EXTRA_LOCATION)
            }
        return location
    }

    override fun onReceive(context: Context, intent: Intent) {
        val location = location4Intent(intent)
        Log.i(__CLASSNAME__, "${getMethodName()}$location, $context, $intent")
        if (location != null) {
            //val tick = SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSSZ", resources.configuration.locales[0]).format(Date(System.currentTimeMillis()))
            val tick = ""
            logResultsToScreen("${tick} - $location")
        }
    }
}