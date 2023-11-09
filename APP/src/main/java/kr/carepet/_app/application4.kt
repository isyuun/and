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
 *  isyuun@care-pet.kr             2023. 9. 7.   description...
 */

package kr.carepet._app

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

/**
 * @Project     : carepet-android
 * @FileName    : application4.kt
 * @Date        : 2023. 09. 07.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class application4 : application3() {
    private var alert: AlertDialog? = null

    private fun dialog(title: CharSequence, message: CharSequence, positive: DialogInterface.OnClickListener? = null, negative: DialogInterface.OnClickListener? = null, neutral: DialogInterface.OnClickListener? = null) {
        alert?.dismiss()
        alert = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(title)
                setMessage(message)
                positive?.let { setPositiveButton(android.R.string.ok, positive) }
                negative?.let { setNegativeButton(android.R.string.cancel, negative) }
                neutral?.let { setNeutralButton(android.R.string.ok, neutral) }
            }

            // Set other dialog properties
            //TODO

            // Create the AlertDialog
            builder.create()
        }
    }

    fun ok(message: CharSequence, positive: DialogInterface.OnClickListener) {
        dialog(getString(android.R.string.ok), message, positive)
    }

    fun cancel(message: CharSequence, negative: DialogInterface.OnClickListener) {
        dialog(getString(android.R.string.ok), message, null, negative)
    }

    fun show(message: CharSequence, neutral: DialogInterface.OnClickListener) {
        dialog(getString(android.R.string.ok), message, null, null, neutral)
    }
}