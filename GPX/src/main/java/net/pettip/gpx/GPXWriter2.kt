/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 13.   description...
 */

package net.pettip.gpx

import java.io.File
import java.util.Date

/**
 * @Project     : carepet-android
 * @FileName    : GPXWriter2.kt
 * @Date        : 2023. 09. 13.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
class GPXWriter2 : _GPXWriter() {
    companion object {
        fun write(tracks: List<Track>, file: File) {
            if (tracks.isEmpty()) {
                return
            }
            val firstTime = GPX_SIMPLE_TICK_FORMAT.format(Date(tracks.first().time))

            val comment = """
                <!-- Created with PetTip -->
                <!-- Track = ${tracks.size} TrackPoints + 0 Placemarks -->
                <!-- Track Statistics (based on Total Time | Time in Movement): -->
                <!-- Distance = ${calculateTotalDistance(tracks)} -->
                <!-- Duration = ${calculateDuration(tracks)} | N/A -->
                <!-- Altitude Gap = ${calculateMaxAltitudeGap(tracks)} -->
                <!-- Max Speed = ${calculateMaxSpeed(tracks)} m/s -->
                <!-- Avg Speed = ${calculateAvgSpeed(tracks)} | N/A -->
                <!-- Direction = N/A -->
                <!-- Activity = N/A -->
                <!-- Altitudes = N/A -->
            """.trimIndent() + "\n"

            val metadata = """
                <metadata>
                 <name>PetTip $firstTime</name>
                 <time>${GPX_SIMPLE_DATE_FORMAT.format(tracks.first().time)}</time>
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

            for (track in tracks) {
                val lat = GPX_DECIMAL_FORMAT_7.format(track.latitude)
                val lon = GPX_DECIMAL_FORMAT_7.format(track.longitude)
                val time = GPX_SIMPLE_DATE_FORMAT.format(track.time)
                val speed = GPX_DECIMAL_FORMAT_3.format(track.speed)
                val ele = GPX_DECIMAL_FORMAT_3.format(track.altitude)
                val no = track.no
                val pee = track.pee
                val poo = track.poo
                val mrk = track.mrk
                val img = track.img
                //val uri = track.uri
                val uri = ""
                var event = Track.EVENT.nnn
                if (pee > TRACK_ZERO_PEE) event = Track.EVENT.pee
                if (poo > TRACK_ZERO_POO) event = Track.EVENT.poo
                if (mrk > TRACK_ZERO_MRK) event = Track.EVENT.mrk
                if (img > TRACK_ZERO_IMG) event = Track.EVENT.img
                val trkpt = """ <trkpt no="${no}" event="${event}" lat="${lat}" lon="${lon}"><time>$time</time><speed>$speed</speed><ele>$ele</ele><uri>$uri</uri></trkpt>""" + "\n"
                trksegStringBuilder.append(trkpt)
            }

            val trkseg = "<trkseg>\n$trksegStringBuilder</trkseg>\n"
            val trk = "<trk>\n<name>$firstTime</name>\n$trkseg</trk>\n"
            val content = "$header$metadata$trk$footer"

            file.writeText(comment + content)
        }
    }
}
