/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package net.pettip.gps.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import net.pettip.gps._app.gpscomponentactivity4
import net.pettip.gps.app.GPSApplication.Companion.permissions
import net.pettip.util.Log
import net.pettip.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : GPSComponentActivity.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class GPSComponentActivity : gpscomponentactivity4() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        Log.i(__CLASSNAME__, "${getMethodName()}[${it}]")
        var granted = true
        for (entry in it.entries) {
            Log.w(__CLASSNAME__, "${getMethodName()}[${entry.key}][${entry.value}]")
            if (!entry.value) granted = false
        }
        if (!granted) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:$packageName"))
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(__CLASSNAME__, "${getMethodName()}$permissions")
        super.onCreate(savedInstanceState)
        requestPermission.launch(permissions)
    }
}