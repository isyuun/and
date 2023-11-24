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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import net.pettip.gps.app.ForegroundOnlyBroadcastReceiver2
import net.pettip.gps.app.IForegroundOnlyBroadcastReceiver
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.text.SimpleDateFormat
import java.util.Date

/**
 * @Project     : carepet-android
 * @FileName    : gpsapplication2.kt
 * @Date        : 2023. 09. 07.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class gpsapplication2 : gpsapplication(), IForegroundOnlyBroadcastReceiver {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    internal fun registerReceiver2(receiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver,
            IntentFilter(
                ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
            )
        )
    }

    internal fun unregisterReceiver2(receiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            receiver
        )
    }

    private fun registerReceiver() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$receiver")
        registerReceiver2(receiver)
    }

    private fun unregisterReceiver() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}$receiver")
        unregisterReceiver2(receiver)
    }

    override fun onResume() {
        Log.w(__CLASSNAME__, "${getMethodName()}$receiver")
        registerReceiver()
    }

    override fun onPause() {
        Log.w(__CLASSNAME__, "${getMethodName()}$receiver")
        unregisterReceiver()
    }

    // Listens for location broadcasts from ForegroundOnlyBroadcastReceiver2.
    private lateinit var receiver: ForegroundOnlyBroadcastReceiver2

    override fun init() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}...")
        super.init()
        receiver = ForegroundOnlyBroadcastReceiver2(this)
        Log.w(__CLASSNAME__, "${getMethodName()}$receiver")
    }

    internal fun location4Intent(intent: Intent): Location? {
        val location =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(EXTRA_LOCATION, Location::class.java)
            } else {
                intent.getParcelableExtra(EXTRA_LOCATION)
            }
        return location
    }

    override fun onReceive(context: Context, intent: Intent) {
        val location = location4Intent(intent)
        Log.w(__CLASSNAME__, "${getMethodName()}${location.toText()}, $location, $context, $intent")
        if (location != null) {
            val tick = SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSSZ", resources.configuration.locales[0]).format(Date(System.currentTimeMillis()))
            logResultsToScreen("${tick} - ${location.toText()}, $location")
        }
    }
}