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

import android.location.Location

/**
 * @Project     : carepet-android
 * @FileName    : Track.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
data class Track(
    private val loc: Location,
    val id: String = "",
    val img: Int = -1,
    val pee: Int = 0,
    val poo: Int = 0,
    val mrk: Int = 0,
) {
    fun toText(): String {
        return "($latitude, $longitude)"
    }

    enum class EVENT {
        nnn, img, pee, poo, mrk
    }

    val event : EVENT
        get() {
            return if (img > -1) EVENT.img
            else if (pee > 0) EVENT.pee
            else if (poo > 0) EVENT.poo
            else if (mrk > 0) EVENT.mrk
            else EVENT.nnn
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
