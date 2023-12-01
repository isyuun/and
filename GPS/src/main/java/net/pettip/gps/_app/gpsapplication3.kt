/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 10. 18.   description...
 */

package net.pettip.gps._app

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.net.Uri
import android.os.IBinder
import net.pettip.data.pet.CurrentPetData
import net.pettip.gpx.TRACK_ZERO_NUM
import net.pettip.gpx.Track
import net.pettip.util.Log
import net.pettip.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : gpsapplication4.kt
 * @Date        : 2023. 10. 18.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class gpsapplication3 : gpsapplication2() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.w(__CLASSNAME__, "${getMethodName()}[${this.activity}][${(this.activity is ServiceConnection)}]")
        super.onServiceConnected(name, service)
        this.service?.onServiceConnected(name, service)
        if (this.activity is ServiceConnection) (this.activity as ServiceConnection).onServiceConnected(name, service)
        Log.v(__CLASSNAME__, "${getMethodName()}[${activity?.intent}][${this.service}][${this.service?.launchActivityIntent}]")
        super.onServiceConnected(name, service)
        this.service?.launchActivityIntent = activity?.intent
        Log.v(__CLASSNAME__, "${getMethodName()}[${activity?.intent}][${this.service}][${this.service?.launchActivityIntent}]")
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.w(__CLASSNAME__, "${getMethodName()}[${this.activity}][${(this.activity is ServiceConnection)}]")
        super.onServiceDisconnected(name)
        this.service?.onServiceDisconnected(name)
        if (this.activity is ServiceConnection) (this.activity as ServiceConnection).onServiceDisconnected(name)
    }

    override fun start() {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no][$start]")
        if (start) return
        super.start()
        tracks?.clear()
        images?.clear()
        preview = null
    }

    override fun stop() {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no][$start]")
        super.stop()
    }

    val pause
        get() = (this.service?.start == true && this.service?.pause == true)

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

    val lastLocation
        get() = this.service?.lastLocation

    val __duration
        get() = this.service?.__duration

    val _duration
        get() = this.service?._duration

    val duration
        get() = this.service?.duration

    val _distance
        get() = this.service?._distance

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
        if (!this.pets.isEmpty()) select(this.pets.last()) else no = TRACK_ZERO_NUM
    }

    private fun select(pet: CurrentPetData) {
        if (this.pets.contains(pet)) no = pet.ownrPetUnqNo
    }

    fun contains(pet: CurrentPetData): Boolean {
        return this.pets.contains(pet)
    }

    private var no: String
        get() = this.service?.no ?: TRACK_ZERO_NUM
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
            Track.EVENT.NNN -> {}
            Track.EVENT.IMG -> {}
            Track.EVENT.PEE -> pee(pet)
            Track.EVENT.POO -> poo(pet)
            Track.EVENT.MRK -> mrk(pet)
        }
    }

    fun rotate(context: Context, uri: Uri) = this.service?.rotate(context, uri)

    fun orient(context: Context, uri: Uri) = this.service?.orient(context, uri)

    var preview: Bitmap? = null
}