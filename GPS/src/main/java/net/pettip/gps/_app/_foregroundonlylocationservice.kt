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
import androidx.core.content.edit
import net.pettip.app.Service

private const val PACKAGE_NAME = "net.pettip.app.navi"

internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
    "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

internal const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
    "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"

internal const val NOTIFICATION_ID = 12345678

internal const val NOTIFICATION_CHANNEL_ID = "net.pettip.app.navi.channel.01"

internal const val SHARED_PREFERENCE_FILE_KEY = "$PACKAGE_NAME.PREFERENCE_FILE_KEY"

internal const val KEY_FOREGROUND_ENABLED = "tracking_foreground_location"
internal const val KEY_FOREGROUND_GPXFILE = "tracking.foreground.gpx.flie"

/**
 * Provides access to SharedPreferences for location to Activities and Services.
 */
private object SharedPreferenceUtil {

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The [Context].
     */
    fun getLocationTrackingPref(context: Context): Boolean =
        context.getSharedPreferences(
            SHARED_PREFERENCE_FILE_KEY,
            Context.MODE_PRIVATE
        ).getBoolean(KEY_FOREGROUND_ENABLED, false)

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) =
        context.getSharedPreferences(
            SHARED_PREFERENCE_FILE_KEY,
            Context.MODE_PRIVATE
        ).edit {
            putBoolean(KEY_FOREGROUND_ENABLED, requestingLocationUpdates)
        }
}


/**
 * @Project     : carepet-android
 * @FileName    : _foregroundonlylocationservice.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class _foregroundonlylocationservice : Service()