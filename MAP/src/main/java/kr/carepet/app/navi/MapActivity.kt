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
 *  isyuun@care-pet.kr             2023. 9. 4.   description...
 */

package kr.carepet.app.navi

/**import kr.carepet.util.__CLASSNAME__*/
import android.content.Intent
import androidx.activity.compose.setContent
import kr.carepet.app.navi.ui.theme.AppTheme
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : MainActivity.kt
 * @Date        : 2023. 09. 04.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class MapActivity : kr.carepet.map.app.MapActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun setContent() {
        Log.v(__CLASSNAME__, "${getMethodName()}")
        //super.setContent()
        setContent {
            AppTheme(dynamicColor = true) {
                MapApp()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        Log.wtf(__CLASSNAME__, "${getMethodName()}[$intent]")
    }
}