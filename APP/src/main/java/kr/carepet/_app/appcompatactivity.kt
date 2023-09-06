/*
 *  Copyright 2011 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
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
    //private val __CLASSNAME__ = Exception().stackTrace[0].fileName

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