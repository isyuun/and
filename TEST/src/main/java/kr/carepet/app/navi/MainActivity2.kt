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
 *  isyuun@care-pet.kr             2023. 8. 10.   description...
 */

package kr.carepet.app.navi

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.parcelize.Parcelize
import kr.carepet.app.navi.ui.theme.AndTheme
import kr.carepet.util.Log

/**
 * @Project     : carepet-android
 * @FileName    : MainActivity2.kt
 * @Date        : 2023. 08. 10.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class MainActivity2 : MainActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.Companion.wtf(__CLASSNAME__, "${getMethodName()}:${this@MainActivity2.intent}")
        super.onCreate(savedInstanceState)
        setContent { setContent() }
        Log.Companion.wtf(__CLASSNAME__, "${getMethodName()}:${this@MainActivity2.intent}")
    }

    @Preview
    @Composable
    fun setContent() {
        Log.Companion.wtf(__CLASSNAME__, "${getMethodName()}:${this@MainActivity2.intent}")
        AndTheme {
            // A surface container using the 'background' color from the theme
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Greeting("Android")
                    Root()
                }
            }
        }
        Log.Companion.wtf(__CLASSNAME__, "${getMethodName()}:${this@MainActivity2.intent}")
    }
}

private val __CLASSNAME__ = Exception().stackTrace[0].fileName
fun getMethodName(): String {
    val stack = Thread.currentThread().stackTrace[3]
    val className = stack.className
    val methodName = stack.methodName
    val path = "${className}.${methodName}"
    val file = stack.fileName
    val line = stack.lineNumber
    return "${path}(${file}:${line}) "
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