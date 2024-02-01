/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 14.   description...
 */

package net.pettip.gpx

import android.location.Location
import android.net.Uri

/**
 * @Project     : carepet-android
 * @FileName    : Track.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
const val TRACK_ZERO_NUM = "P00000000000000"
val TRACK_ZERO_URI = Uri.parse("")!!

/** <a hreef="https://stackoverflow.com/questions/43080343/calculate-distance-between-two-tracks-in-metre">Calculate distance between two tracks in metre</a> */
fun distance(trk1: Track?, trk2: Track?): Float {
    if (trk1 == null || trk2 == null) return 0.0f
    val lat1: Double = trk1.latitude
    val lon1: Double = trk1.longitude
    val lat2: Double = trk2.latitude
    val lon2: Double = trk2.longitude
    val distances = FloatArray(2)
    Location.distanceBetween(
        lat1, lon1,
        lat2, lon2,
        distances
    )
    return distances[0]
}

fun MutableList<Track>._duration(): Long {
    if (this.isEmpty()) {
        return 0L
    }
    val startTime = this.first().time
    val endTime = this.last().time
    return endTime - startTime
}

fun MutableList<Track>._distance(): Float {
    if (this.isEmpty()) {
        return 0.0f
    }
    var totalDistance = 0.0f
    this.let {
        for (i in 1 until it.size) {
            val prevLocation = it[i - 1]
            val currentLocation = it[i]
            val distance = prevLocation.location.distanceTo(currentLocation.location)
            totalDistance += distance
        }
    }
    return totalDistance
}

data class Track(
    private val loc: Location,
    val no: String = TRACK_ZERO_NUM,
    val event: EVENT = EVENT.NNN,
    val uri: Uri = TRACK_ZERO_URI,
) {
    companion object {

    }

    fun toText(): String {
        return "($latitude, $longitude)"
    }

    enum class EVENT {
        NNN, IMG, PEE, POO, MRK
    }

    val location: Location
        get() = loc

    val latitude: Double
        get() = loc.latitude

    val longitude: Double
        get() = loc.longitude

    val time: Long
        get() = loc.time

    val speed: Float
        get() = loc.speed

    val altitude: Double
        get() = loc.altitude
}
