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
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : CameraContentObserver.kt
 * @Date        : 2023. 09. 18.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
class CameraContentObserver(private val context: foregroundonlylocationservice4, handler: Handler) : ContentObserver(handler) {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName
    override fun deliverSelfNotifications(): Boolean {
        Log.v(__CLASSNAME__, "${getMethodName()}...")
        return super.deliverSelfNotifications()
    }

    override fun onChange(selfChange: Boolean) {
        Log.v(__CLASSNAME__, "${getMethodName()}[selfChange:$selfChange]")
        super.onChange(selfChange)
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        Log.v(__CLASSNAME__, "${getMethodName()}[selfChange:$selfChange][uri:$uri]")
        super.onChange(selfChange, uri)
        uri?.let { context.onChange(selfChange, it) }
    }

    override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
        Log.v(__CLASSNAME__, "${getMethodName()}[selfChange:$selfChange][uri:$uri][flags:$flags]")
        super.onChange(selfChange, uri, flags)
        uri?.let { context.onChange(selfChange, it) }
    }

    override fun onChange(selfChange: Boolean, uris: MutableCollection<Uri>, flags: Int) {
        Log.v(__CLASSNAME__, "${getMethodName()}[selfChange:$selfChange][uris:$uris][flags:$flags]")
        super.onChange(selfChange, uris, flags)
        uris.forEach { uri -> uri?.let { context.onChange(selfChange, it) } }
    }
}