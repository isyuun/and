/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.gps._app

import kr.carepet.app.Service

private const val PACKAGE_NAME = "kr.carepet.app.navi"

internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
    "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

internal const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
    "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"

internal const val NOTIFICATION_ID = 12345678

internal const val NOTIFICATION_CHANNEL_ID = "kr.carepet.app.navi.channel.01"

internal const val SHARED_PREFERENCE_FILE_KEY = "$PACKAGE_NAME.PREFERENCE_FILE_KEY"

/**
 * @Project     : carepet-android
 * @FileName    : _foregroundonlylocationservice.kt
 * @Date        : 2023. 09. 14.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class _foregroundonlylocationservice : Service()