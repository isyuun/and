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
 *  isyuun@care-pet.kr             2023. 8. 17.   description...
 */

package kr.carepet.app.navi

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.carepet.app.navi.ui.theme.AndTheme
import kr.carepet.location.bglocationaccess.BgLocationAccessScreen
import kr.carepet.util.Log
import kr.carepet.util.getMethodName
import java.util.concurrent.TimeUnit

private val __CLASSNAME__ = Exception().stackTrace[0].fileName

/**
 * @Project     : carepet-android
 * @FileName    : MainActivity3.kt
 * @Date        : 2023. 08. 17.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class MainActivity3 : MainActivity2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Content() }
        post { init() }
    }

    protected fun init() {
    }
}

@Preview
@Composable
internal fun Content() {
    AndTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Log.d(__CLASSNAME__, "${getMethodName()}")
            BgLocationAccessScreen()
        }
    }
}
