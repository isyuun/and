/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.6
 *
 */

package net.pettip.gpx

import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.util.Xml
import org.joda.time.format.ISODateTimeFormat
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * @Project     : PetTip-Android
 * @FileName    : GPXParser
 * @Date        : 2023-12-06
 * @author      : isyuun
 * @description : net.pettip.gpx
 * @see net.pettip.gpx.GPXParser
 */
class GPXParser(private val tracks: MutableList<Track>) : _GPX() {
    companion object {
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun read(file: File) {
        read(file.inputStream())
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun read(`in`: InputStream) {
        try {
            `in`.use {
                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
                parser.setInput(it, null)
                parser.nextTag()
                readGpx(parser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loopMustContinue(next: Int): Boolean {
        return next != XmlPullParser.END_TAG && next != XmlPullParser.END_DOCUMENT
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readGpx(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, namespace, TAG_GPX)
        while (loopMustContinue(parser.next())) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            val name = parser.name
            //println("read()-$name")
            when (name) {
                TAG_TRACK -> readTrack(parser)
                else -> skip(parser)
            }
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTrack(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, namespace, TAG_TRACK)
        while (loopMustContinue(parser.next())) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            val name = parser.name
            //println("\tread() $name")
            when (name) {
                TAG_SEGMENT -> readSegment(parser)
                else -> skip(parser)
            }
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readSegment(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, namespace, TAG_SEGMENT)
        while (loopMustContinue(parser.next())) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            val name = parser.name
            //println("\t\tread() $name")
            when (name) {
                TAG_TRACK_POINT -> readTrackPoint(parser)
                else -> skip(parser)
            }
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTrackPoint(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, namespace, TAG_TRACK_POINT)
        val lat = parser.getAttributeValue(namespace, TAG_LAT).toDouble()
        val lon = parser.getAttributeValue(namespace, TAG_LON).toDouble()
        val no = parser.getAttributeValue(namespace, "no")
        val event = Track.EVENT.valueOf(parser.getAttributeValue(namespace, "event").uppercase())
        var time = 0L
        var speed = 0.0f
        var bearing = 0.0f
        var ele = 0.0
        var uri = ""
        while (loopMustContinue(parser.next())) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            val name = parser.name
            val text = readText(parser)
            //println("\t\t\tread() $name - $text")
            when (name) {
                TAG_TIME -> time = parseDateTime(text)
                TAG_SPEED -> speed = if (text.isNotEmpty()) text.toFloat() else 0.0f
                TAG_ELEVATION -> ele = if (text.isNotEmpty()) text.toDouble() else 0.0
                TAG_BEARING -> bearing = if (text.isNotEmpty()) text.toFloat() else 0.0f
                TAG_URI -> uri = text
            }
        }
        val loc = Location(LocationManager.GPS_PROVIDER)
        loc.latitude = lat
        loc.longitude = lon
        loc.time = time
        loc.speed = speed
        loc.altitude = ele
        loc.bearing = bearing
        val track = Track(loc = loc, no = no, event = event, uri = Uri.parse(uri))
        //println("read() [${GPX_DATE_FORMAT.format(time)}][$track]")
        tracks.add(track)
    }

    private fun parseDateTime(text: String): Long {
        if (text.isEmpty()) return 0L
        return try {
            GPX_DATE_FORMAT.parse(text)?.time ?: 0L
        } catch (e: Exception) {
            //println(e.message)
            val time = ISODateTimeFormat.dateTimeParser().parseDateTime(text)
            (time.millis - 9 * 3600 * 1000)
        }
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }
}