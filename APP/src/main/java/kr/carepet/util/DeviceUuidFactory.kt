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
 *  isyuun@care-pet.kr             2023. 8. 10.   description...
 */

package kr.carepet.util

import android.content.Context
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import java.io.UnsupportedEncodingException
import java.util.UUID


/**
 * @Project     : carepet-android
 * @FileName    : DeviceUuidFactory.kt
 * @Date        : 2023. 08. 10.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class DeviceUuidFactory(context: Context) {

    companion object {
        const val PREFS_FILE = "device_id.xml"
        const val PREFS_DEVICE_ID = "device_id"
    }

    @Volatile
    protected var uuid: UUID? = null

    init {
        if (uuid == null) {
            synchronized(DeviceUuidFactory::class.java) {
                if (uuid == null) {
                    val prefs = context.getSharedPreferences(PREFS_FILE, 0)
                    val id = prefs.getString(PREFS_DEVICE_ID, null)
                    if (id != null) {
                        // Use the ids previously computed and stored in the
                        // prefs file
                        uuid = UUID.fromString(id)
                    } else {
                        val androidId = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
                        // Use the Android ID unless it's broken, in which case
                        // fallback on deviceId,
                        // unless it's not available, then fallback on a random
                        // number which we store to a prefs file
                        uuid = try {
                            if ("9774d56d682e549c" != androidId) {
                                UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
                            } else {
                                /**
                                 * This method was deprecated in API level 26.
                                 * Use getImei() which returns IMEI for GSM or getMeid() which returns MEID for CDMA.
                                 * <a href="https://developer.android.com/reference/android/telephony/TelephonyManager.html#getDeviceId()"></a>
                                 */
                                val deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                                //val deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).imei
                                //val deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).meid
                                if (deviceId != null) UUID.nameUUIDFromBytes(deviceId.toByteArray(charset("utf8")))
                                else UUID.randomUUID()
                            }
                        } catch (e: UnsupportedEncodingException) {
                            throw RuntimeException(e)
                        }
                        // Write the value out to the prefs file
                        prefs.run { edit().putString(PREFS_DEVICE_ID, uuid.toString()).apply() }
                    }
                }
            }
        }
    }

    fun getDeviceUuid(): UUID? {
        return uuid
    }
}