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
 *  isyuun@care-pet.kr             2023. 10. 18.   description...
 */

package kr.carepet.gps._app

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.IBinder
import kr.carepet.data.pet.CurrentPetData
import kr.carepet.gpx.TRACK_ZERO_NO
import kr.carepet.gpx.Track
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : gpsapplication4.kt
 * @Date        : 2023. 10. 18.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class gpsapplication4 : gpsapplication3() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun start() {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no][$start]")
        if (start) return
        super.start()
        tracks?.clear()
        images?.clear()
    }

    override fun stop() {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no][$start]")
        super.stop()
    }

    fun pause() {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no][$start]")
        this.service?.pause()
    }

    fun resume() {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no][$start]")
        this.service?.resume()
    }

    val path
        get() = this.service?.path

    val file
        get() = this.service?.file

    val images
        get() = this.service?.images

    val tracks
        get() = this.service?.tracks

    val _duration
        get() = this.service?._duration

    val _distance
        get() = this.service?._distance

    val duration
        get() = this.service?.duration

    val distance
        get() = this.service?.distance

    val pets = ArrayList<CurrentPetData>()

    fun add(pet: CurrentPetData) {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.pets.contains(pet)}][$no]$pet$pets")
        if (!this.pets.contains(pet)) this.pets.add(pet)
        select()
    }

    fun add(pets: List<CurrentPetData>) {
        Log.i(__CLASSNAME__, "${getMethodName()}$pets")
        this.pets.clear(); this.pets.addAll(pets)
        select()
    }

    fun remove(pet: CurrentPetData) {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.pets.contains(pet)}][$no]$pet$pets")
        if (this.pets.contains(pet)) this.pets.remove(pet)
        select()
    }

    fun remove() {
        Log.i(__CLASSNAME__, "${getMethodName()}$pets")
        this.pets.clear()
        select()
    }

    private fun select() {
        if (!this.pets.isEmpty()) select(this.pets.last()) else no = TRACK_ZERO_NO
    }

    private fun select(pet: CurrentPetData) {
        if (this.pets.contains(pet)) no = pet.ownrPetUnqNo
    }

    fun contains(pet: CurrentPetData): Boolean {
        return this.pets.contains(pet)
    }

    private var no: String
        get() = this.service?.no ?: TRACK_ZERO_NO
        set(no) {
            this.service?.no = no
        }

    private fun pee(pet: CurrentPetData) {
        select(pet)
        Log.d(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no]")
        this.service?.pee()
    }

    private fun poo(pet: CurrentPetData) {
        select(pet)
        Log.d(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no]")
        this.service?.poo()
    }

    private fun mrk(pet: CurrentPetData) {
        select(pet)
        Log.d(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no]")
        this.service?.mrk()
    }

    fun mark(pet: CurrentPetData, event: Track.EVENT) {
        when (event) {
            Track.EVENT.nnn -> {}
            Track.EVENT.img -> {}
            Track.EVENT.pee -> pee(pet)
            Track.EVENT.poo -> poo(pet)
            Track.EVENT.mrk -> mrk(pet)
        }
    }

    fun rotate(context: Context, uri: Uri) = this.service?.rotate(context, uri)

    fun orient(context: Context, uri: Uri) = this.service?.orient(context, uri)

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.v(__CLASSNAME__, "${getMethodName()}[${getActivity()?.intent}][${this.service}][${this.service?.launchActivityIntent}]")
        super.onServiceConnected(name, service)
        this.service?.launchActivityIntent = getActivity()?.intent
        Log.v(__CLASSNAME__, "${getMethodName()}[${getActivity()?.intent}][${this.service}][${this.service?.launchActivityIntent}]")
    }
}