/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 12.   description...
 */

package net.pettip.gpx

import android.location.Location
import java.io.File
import java.io.FileWriter
import java.io.IOException


/**
 * @Project     : carepet-android
 * @FileName    : GPXWriter.kt
 * @Date        : 2023. 09. 12.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
class GPXWriter {

    companion object {
        fun write(locations: List<Location>, gpxFile: File?) {
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