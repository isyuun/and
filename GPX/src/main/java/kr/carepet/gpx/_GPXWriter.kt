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
 *  isyuun@care-pet.kr             2023. 9. 13.   description...
 */

package kr.carepet.gpx

/**
 * @Project     : carepet-android
 * @FileName    : _GPXWriter.kt
 * @Date        : 2023. 09. 13.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

const val GPX_VERSION = "1.1"
const val GPX_CREATOR = "CarePet"
const val GPX_NAMESPACE = "http://www.topografix.com/GPX/1/1"
const val GPX_XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance"
val GPX_SIMPLE_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA)
val GPX_SIMPLE_TICK_FORMAT = SimpleDateFormat("yyyyMMdd.HHmmss", Locale.KOREA)
val GPX_DECIMAL_FORMAT_3 = DecimalFormat("0.000")
val GPX_DECIMAL_FORMAT_7 = DecimalFormat("0.0000000")

const val GPX_LATITUDE_ZERO_KO = 127.054136
const val GPX_LONGITUDE_ZERO_KO = 37.275935

open class _GPXWriter {
    companion object {
        @JvmStatic
        protected fun calculateTotalDistance(locations: List<Location>): String {
            var totalDistance = 0.0
            for (i in 1 until locations.size) {
                val prevLocation = locations[i - 1]
                val currentLocation = locations[i]
                val distance = prevLocation.location.distanceTo(currentLocation.location)
                totalDistance += distance
            }
            return String.format("%.2f km", totalDistance / 1000)
        }

        @JvmStatic
        protected fun calculateDuration(locations: List<Location>): String {
            if (locations.isEmpty()) {
                return "N/A"
            }

            val startTime = locations.first().location.time
            val endTime = locations.last().location.time
            val durationInMillis = endTime - startTime
            val seconds = durationInMillis / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
        }

        @JvmStatic
        protected fun calculateMaxAltitudeGap(locations: List<Location>): String {
            if (locations.isEmpty()) {
                return "N/A"
            }

            var maxAltitudeGap = 0.0
            for (i in 1 until locations.size) {
                val prevLocation = locations[i - 1]
                val currentLocation = locations[i]
                val altitudeGap = Math.abs(currentLocation.location.altitude - prevLocation.location.altitude)
                if (altitudeGap > maxAltitudeGap) {
                    maxAltitudeGap = altitudeGap
                }
            }
            return String.format("%.2f m", maxAltitudeGap)
        }

        @JvmStatic
        protected fun calculateMaxSpeed(locations: List<Location>): String {
            if (locations.isEmpty()) {
                return "N/A"
            }

            var maxSpeed = 0.0f
            for (location in locations) {
                if (location.location.speed > maxSpeed) {
                    maxSpeed = location.location.speed
                }
            }
            return String.format("%.2f m/s", maxSpeed)
        }

        @JvmStatic
        protected fun calculateAvgSpeed(locations: List<Location>): String {
            if (locations.isEmpty()) {
                return "N/A"
            }

            var totalSpeed = 0.0f
            for (location in locations) {
                totalSpeed += location.location.speed
            }
            val avgSpeed = totalSpeed / locations.size
            return String.format("%.2f m/s", avgSpeed)
        }
    }
}
