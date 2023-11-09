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

import android.os.Bundle

/**
 * @Project     : carepet-android
 * @FileName    : appcompatactivity.kt
 * @Date        : 2023. 08. 21.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class appcompatactivity : androidx.appcompat.app.AppCompatActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private lateinit var application: application
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = this.applicationContext as application
    }

    override fun onResume() {
        super.onResume()
        application.setActivity(this)
    }

    override fun onPause() {
        clearReferences()
        super.onPause()
    }

    override fun onDestroy() {
        clearReferences()
        super.onDestroy()
    }

    private fun clearReferences() {
        if (this == application.getActivity()) application.setActivity(null)
    }
}