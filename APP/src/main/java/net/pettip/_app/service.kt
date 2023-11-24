/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 11.   description...
 */

package net.pettip._app

import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * @Project     : carepet-android
 * @FileName    : service.kt
 * @Date        : 2023. 09. 11.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class service : android.app.Service() {
    private val localBinder = LocalBinder()

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: service = this@service
    }

    override fun onBind(intent: Intent): IBinder {
        return localBinder
    }
}