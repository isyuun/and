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
 *  isyuun@care-pet.kr             2023. 9. 27.   description...
 */

package com.example.test

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.parcelize.Parcelize
import kr.carepet.app.navi.ui.theme.AndTheme
import kr.carepet.util.Log
/**import kr.carepet.util.__CLASSNAME__*/
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : MainActivity2.kt
 * @Date        : 2023. 08. 16.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
private val __CLASSNAME__ = Exception().stackTrace[0].fileName

open class MainActivity2 : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(__CLASSNAME__, "${getMethodName()}$savedInstanceState")
        super.onCreate(savedInstanceState)
        //setContent { Content(savedInstanceState) }
    }

}

@Composable
fun Content(savedInstanceState: Bundle?) {
    Log.i(__CLASSNAME__, "${getMethodName()}$savedInstanceState")
    AndTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Greeting("Android")
            Root()
        }
    }
    Log.i(__CLASSNAME__, "${getMethodName()}$savedInstanceState")
}

@Parcelize
data class TestData(val num: Int) : Parcelable

var var1 = 0

@Composable
fun Root() {
    var data by remember { mutableStateOf(TestData(0)) }
    Log.w(__CLASSNAME__, "${getMethodName()} data = ${data.num}, var1 = $var1")
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                Log.wtf(__CLASSNAME__, "${getMethodName()} data = ${data.num}, var1 = $var1")
                data = TestData(data.num + 1); var1++
                Log.wtf(__CLASSNAME__, "${getMethodName()} data = ${data.num}, var1 = $var1")
            },
        ) {
            Log.i(__CLASSNAME__, "${getMethodName()} data = ${data.num}, var1 = $var1")
            Text("CLICK")
        }
        Log.w(__CLASSNAME__, "${getMethodName()} data = ${data.num}, var1 = $var1")
        Text("data = ${data.num}, var1 = $var1")
    }
    Log.w(__CLASSNAME__, "${getMethodName()} data = ${data.num}, var1 = $var1")
}