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
 *  isyuun@care-pet.kr             2023. 9. 12.   description...
 */

package kr.carepet.gpx

import android.location.Location
import java.io.File
import java.io.FileWriter
import java.io.IOException


/**
 * @Project     : carepet-android
 * @FileName    : GPXWriter.kt
 * @Date        : 2023. 09. 12.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
class GPXWriter {

    companion object {
        fun saveLocationsToGPX(locations: List<Location>, gpxFile: File?) {
            try {
                val writer = FileWriter(gpxFile)
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n")
                writer.write("<gpx version=\"1.1\" xmlns=\"http://www.topografix.com/GPX/1/1\">\n")
                for (location in locations) {
                    writer.write(
                        """<wpt lat="${location.latitude}" lon="${location.longitude}">
"""
                    )
                    writer.write("</wpt>\n")
                }
                writer.write("</gpx>\n")
                writer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}