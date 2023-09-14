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
 *  isyuun@care-pet.kr             2023. 9. 14.   description...
 */

package kr.carepet.gpx

/**
 * @Project     : carepet-android
 * @FileName    : Location.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
data class Location(
    val location: android.location.Location,
    val pee: Int = 0,
    val poo: Int = 0
) {
    fun toText(): String {
        return if (this != null) {
            "($latitude, $longitude)"
        } else {
            "Unknown location"
        }
    }

    val latitude: Double
        get() = location.latitude

    val longitude: Double
        get() = location.longitude

    val time: Long
        get() = location.time

    val speed: Float
        get() = location.speed

    val altitude: Double
        get() = location.altitude

    companion object {
        fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double, distances: FloatArray) {
            android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, distances)
        }
    }
}
