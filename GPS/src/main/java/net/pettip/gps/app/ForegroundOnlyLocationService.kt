/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 20.   description...
 */

package net.pettip.gps.app

/**import net.pettip.util.__CLASSNAME__*/
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import net.pettip.gps._app.foregroundonlylocationservice6
import net.pettip.util.Log
import net.pettip.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : ForegroundOnlyLocationService.kt
 * @Date        : 2023. 09. 05.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
class ForegroundOnlyLocationService : foregroundonlylocationservice6() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: ForegroundOnlyLocationService
            get() = this@ForegroundOnlyLocationService
    }

    private val localBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        val ret = super.onBind(intent)
        Log.wtf(__CLASSNAME__, "${getMethodName()}[$serviceRunningInForeground], $ret, $localBinder")
        return localBinder
    }
}