/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 8. 21.   description...
 */

package kr.carepet._app

import android.app.Activity

/**
 * @Project     : carepet-android
 * @FileName    : application.kt
 * @Date        : 2023. 08. 21.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class application : android.app.Application() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private var _activity: Activity? = null
    var activity
        get() = this._activity
        set(activity) {
            this._activity = activity
        }
}