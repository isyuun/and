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
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.gps._app

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kr.carepet.data.pet.CurrentPetData
import kr.carepet.gpx.TRACK_ZERO_NO
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : gpsapplication3.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class gpsapplication3 : gpsapplication2() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate() {
        Log.i(__CLASSNAME__, "${getMethodName()}[$no]")
        super.onCreate()
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(__CLASSNAME__, "${getMethodName()}...")
        super.onReceive(context, intent)
        //post { pee("") }    //test
        //post { poo("") }    //test
        //post { mark("") }    //test
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceConnected(name, service)
        this.service?.onServiceConnected(name, service)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.w(__CLASSNAME__, "${getMethodName()}...")
        super.onServiceDisconnected(name)
        this.service?.onServiceDisconnected(name)
    }

    override fun start() {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no]")
        super.start()
    }

    override fun stop() {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no]")
        super.stop()
    }

    fun end() {
        tracks?.clear()
        images?.clear()
        pets.clear()
    }

    val images
        get() = this.service?.imgs

    val tracks
        get() = this.service?.tracks

    val pets = ArrayList<CurrentPetData>()

    fun add(pet: CurrentPetData) {
        Log.i(__CLASSNAME__, "${getMethodName()}[${pets.contains(pet)}][$no]$pet$pets")
        if (!pets.contains(pet)) pets.add(pet)
        if (!pets.isEmpty()) select(pets.last()) else no = TRACK_ZERO_NO
    }

    fun remove(pet: CurrentPetData) {
        Log.i(__CLASSNAME__, "${getMethodName()}[${pets.contains(pet)}][$no]$pet$pets")
        if (pets.contains(pet)) pets.remove(pet)
        if (!pets.isEmpty()) select(pets.last()) else no = TRACK_ZERO_NO
    }

    fun select(pet: CurrentPetData) {
        if (pets.contains(pet)) no = pet.ownrPetUnqNo
        Log.w(__CLASSNAME__, "${getMethodName()}[${pets.contains(pet)}][$no]$pet$pets")
    }

    fun contains(pet: CurrentPetData): Boolean {
        Log.w(__CLASSNAME__, "${getMethodName()}[${pets.contains(pet)}][$no]$pet$pets")
        return pets.contains(pet)
    }

    private var no: String
        get() = this.service?.no ?: TRACK_ZERO_NO
        set(no) {
            this.service?.no = no
        }

    fun pee() {
        Log.d(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no]")
        this.service?.pee()
    }

    fun poo() {
        Log.d(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no]")
        this.service?.poo()
    }

    fun mrk() {
        Log.d(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no]")
        this.service?.mrk()
    }
}