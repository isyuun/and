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
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 8. 17.   description...
 */

package kr.carepet._app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

/**
 * @Project     : carepet-android
 * @FileName    : componentactivity3.kt
 * @Date        : 2023. 08. 17.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class componentactivity3 : componentactivity2() {
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = findViewById<View>(android.R.id.content).rootView
        root = window.decorView.findViewById(android.R.id.content)
    }

    private lateinit var toast: Toast

    fun toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) {
        toast.cancel()
        toast = Toast.makeText(this, text, duration)
        toast.show()
    }

    private lateinit var snackbar: Snackbar

    fun snack(text: CharSequence, duration: Int = Snackbar.LENGTH_LONG) {
        snackbar = Snackbar.make(root, text, duration)
        snackbar.show()
    }
}