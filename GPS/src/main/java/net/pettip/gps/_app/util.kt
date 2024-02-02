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

import android.content.Context
import android.location.Location
import net.pettip.RELEASE
import net.pettip.gpx.GPX_DATE_FORMAT
import net.pettip.gpx.GPX_TICK_FORMAT
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit


fun root(context: Context): String {
    val ret = if (RELEASE) context.filesDir.path else context.getExternalFilesDirs("")[0].path
    return ret
}

fun pics(context: Context): String {
    val ret = "${root(context)}/.PIC"
    return ret
}

fun gpxs(context: Context): String {
    val ret = "${root(context)}/.GPX"
    return ret
}

fun name(time: Long): String {
    return GPX_TICK_FORMAT.format(time)
}

fun date(time: Long): String {
    return GPX_DATE_FORMAT.format(time)
}

/**
 * Returns the `location` object as a human readable string.
 */
fun Location?.toText(): String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}

fun File?.minutes(): Long {
    val lastModifiedMillis = this?.lastModified()
    val lastModifiedInstant = lastModifiedMillis?.let { Instant.ofEpochMilli(it) }
    val lastModifiedDateTime = LocalDateTime.ofInstant(lastModifiedInstant, ZoneId.systemDefault())
    val currentDateTime = LocalDateTime.now()
    return ChronoUnit.MINUTES.between(lastModifiedDateTime, currentDateTime)
}