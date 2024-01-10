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

import android.location.Location
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

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