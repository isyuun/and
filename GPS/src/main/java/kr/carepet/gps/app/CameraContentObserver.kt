/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.gps.app

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import kr.carepet.gps._app.foregroundonlylocationservice4

/**
 * @Project     : carepet-android
 * @FileName    : CameraContentObserver.kt
 * @Date        : 2023. 09. 18.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
class CameraContentObserver(private val context: foregroundonlylocationservice4, handler: Handler) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        uri?.let { context.onChange(selfChange, it) }
    }
}