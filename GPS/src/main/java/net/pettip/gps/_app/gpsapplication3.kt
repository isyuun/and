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
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.IBinder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.pettip.data.pet.CurrentPetData
import net.pettip.gpx.GPX_TICK_FORMAT
import net.pettip.gpx.TRACK_ZERO_NUM
import net.pettip.gpx.Track
import net.pettip.util.Log
import net.pettip.util.getMethodName
import java.io.File

/**
 * @Project     : carepet-android
 * @FileName    : gpsapplication4.kt
 * @Date        : 2023. 10. 18.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class gpsapplication3 : gpsapplication2(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private lateinit var sharedPreferences: SharedPreferences

    fun savePets(pets: ArrayList<CurrentPetData>) {
        val gson = Gson()
        val petsString = gson.toJson(pets)
        Log.v(__CLASSNAME__, "${getMethodName()}[$petsString]")
        sharedPreferences.edit().putString(KEY_FOREGROUND_GPXPETS, petsString).apply()
    }

    fun loadPets(): List<CurrentPetData> {
        val gson = Gson()
        val petsString = sharedPreferences.getString(KEY_FOREGROUND_GPXPETS, null)
        Log.v(__CLASSNAME__, "${getMethodName()}[$petsString]")
        return if (petsString.isNullOrEmpty()) {
            emptyList()
        } else {
            gson.fromJson(petsString, object : TypeToken<List<CurrentPetData>>() {}.type)
        }
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_FILE_KEY, MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        loadPets().forEach { pet -> add(pet) }
        Log.v(__CLASSNAME__, "${getMethodName()}${this.pets}")
    }

    override fun onTerminate() {
        super.onTerminate()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.v(__CLASSNAME__, "${getMethodName()}[$sharedPreferences][$key]")
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.w(__CLASSNAME__, "${getMethodName()}[${this.activity}][${(this.activity is ServiceConnection)}]")
        super.onServiceDisconnected(name)
        this.service?.onServiceDisconnected(name)
        if (this.activity is ServiceConnection) (this.activity as ServiceConnection).onServiceDisconnected(name)
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Log.w(__CLASSNAME__, "${getMethodName()}[${this.activity}][${(this.activity is ServiceConnection)}]")
        super.onServiceConnected(name, service)
        this.service?.launchActivityIntent = activity?.intent
        this.service?.onServiceConnected(name, service)
        Log.v(__CLASSNAME__, "${getMethodName()}[${activity?.intent}][${this.service}][${this.service?.launchActivityIntent}]")
        if (this.activity is ServiceConnection) (this.activity as ServiceConnection).onServiceConnected(name, service)
    }

    fun read(file: File) = service?.read(file)

    fun recent(): File? {
        if (service != null) return service?.recent()
        val file = sharedPreferences.getString(KEY_FOREGROUND_GPXFILE, "")?.let { File(it) }
        Log.v(__CLASSNAME__, "${getMethodName()}[${file?.exists()}][${file?.minutes()}:$GPS_RELOAD_MINUTES][${file}]")
        return (if (file != null && file.exists() && file.minutes() < GPS_RELOAD_MINUTES) file else null)
    }

    fun reset() {
        if (service != null) service?.reset()
        sharedPreferences.edit().putString(KEY_FOREGROUND_GPXFILE, null).apply()
    }

    private fun reload() {
        Log.wtf(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no][$start][recent:${recent()}]")
        recent()?.let { recent -> read(recent) }
    }

    fun restart() {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no][$start]")
        if (start) return
        clear()
        reload()
        super.start()
    }

    override fun start() {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.service?.no}][$no][$start]")
        if (start) return
        clear()
        super.start()
    }

    private fun clear() {
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

    val file: File?
        get() {
            if (tracks?.isEmpty() == true) return null
            try {
                return File("${gpxs(this)}/${GPX_TICK_FORMAT.format(tracks?.first()?.time)}.gpx")
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

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
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.pets.contains(pet)}][$no][$pet]")
        if (!this.pets.contains(pet)) this.pets.add(pet)
        select()
    }

    fun add(pets: List<CurrentPetData>) {
        Log.i(__CLASSNAME__, "${getMethodName()}[$pets]")
        this.pets.clear(); this.pets.addAll(pets)
        select()
    }

    fun remove(pet: CurrentPetData) {
        Log.i(__CLASSNAME__, "${getMethodName()}[${this.pets.contains(pet)}][$no][$pet]")
        if (this.pets.contains(pet)) this.pets.remove(pet)
        select()
    }

    fun remove(pets: List<CurrentPetData>) {
        Log.i(__CLASSNAME__, "${getMethodName()}[$pets]")
        this.pets.removeAll(pets)
        select()
    }

    fun remove() {
        Log.i(__CLASSNAME__, "${getMethodName()}[$pets]")
        this.pets.clear()
        select()
    }

    private fun select() {
        if (!this.pets.isEmpty()) select(this.pets.last()) else no = TRACK_ZERO_NUM
        savePets(this.pets)
        //Log.i(__CLASSNAME__, "${getMethodName()}${loadPets()}")   //test
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

    val gps
        get() = this.service?.gps()

    val sat
        get() = this.service?.sat()

    val spd
        get() = this.service?.spd()
}