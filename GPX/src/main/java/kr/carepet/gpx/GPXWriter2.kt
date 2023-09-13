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

import android.location.Location
import java.io.File
import java.util.Date

/**
 * @Project     : carepet-android
 * @FileName    : GPXWriter2.kt
 * @Date        : 2023. 09. 13.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
class GPXWriter2 : _GPXWriter() {
    companion object {
        fun write(locations: List<Location>, file: File) {
            if (locations.isEmpty()) {
                return
            }
            val firstTime = GPX_SIMPLE_TICK_FORMAT.format(Date(locations.first().time))

            val comment = """
                <!-- Created with CarePet -->
                <!-- Track = ${locations.size} TrackPoints + 0 Placemarks -->
                <!-- Track Statistics (based on Total Time | Time in Movement): -->
                <!-- Distance = ${calculateTotalDistance(locations)} -->
                <!-- Duration = ${calculateDuration(locations)} | N/A -->
                <!-- Altitude Gap = ${calculateMaxAltitudeGap(locations)} -->
                <!-- Max Speed = ${calculateMaxSpeed(locations)} m/s -->
                <!-- Avg Speed = ${calculateAvgSpeed(locations)} | N/A -->
                <!-- Direction = N/A -->
                <!-- Activity = N/A -->
                <!-- Altitudes = N/A -->
            """.trimIndent() + "\n"

            val metadata = """
                <metadata>
                 <name>CarePet $firstTime</name>
                 <time>${GPX_SIMPLE_DATE_FORMAT.format(locations.first().time)}</time>
                </metadata>
            """.trimIndent() + "\n"

            val header = """
                <gpx version="$GPX_VERSION"
                     creator="$GPX_CREATOR"
                     xmlns="$GPX_NAMESPACE"
                     xmlns:xsi="$GPX_XSI_NAMESPACE"
                     xsi:schemaLocation="$GPX_NAMESPACE http://www.topografix.com/GPX/1/1/gpx.xsd">
            """.trimIndent() + "\n"

            val footer = "</gpx>"

            val trksegStringBuilder = StringBuilder()

            for (location in locations) {
                val lat = GPX_DECIMAL_FORMAT_7.format(location.latitude)
                val lon = GPX_DECIMAL_FORMAT_7.format(location.longitude)
                val time = GPX_SIMPLE_DATE_FORMAT.format(location.time)
                val speed = GPX_DECIMAL_FORMAT_3.format(location.speed)
                val ele = GPX_DECIMAL_FORMAT_3.format(location.altitude)
                val trkpt = """ <trkpt lat="${lat}" lon="${lon}"><time>$time</time><speed>$speed</speed><ele>$ele</ele></trkpt>""" + "\n"
                trksegStringBuilder.append(trkpt)
            }

            val trkseg = "<trkseg>\n$trksegStringBuilder</trkseg>\n"

            val trk = "<trk>\n<name>$firstTime</name>\n$trkseg</trk>\n"

            val content = "$header$metadata$trk$footer"

            file.writeText(comment + content)
        }
    }
}
