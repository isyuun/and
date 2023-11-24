/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 7. 31.   description...
 */

package net.pettip._app

import android.content.DialogInterface.OnClickListener
import androidx.appcompat.app.AlertDialog

/**
 * @Project     : carepet-android
 * @FileName    : appcompatactivity4.kt
 * @Date        : 2023. 07. 31.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class appcompatactivity4 : appcompatactivity3() {
    private var alert: AlertDialog? = null

    private fun dialog(title: CharSequence, message: CharSequence, positive: OnClickListener? = null, negative: OnClickListener? = null, neutral: OnClickListener? = null) {
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

    fun ok(message: CharSequence, positive: OnClickListener) {
        dialog(getString(android.R.string.ok), message, positive)
    }

    fun cancel(message: CharSequence, negative: OnClickListener) {
        dialog(getString(android.R.string.ok), message, null, negative)
    }

    fun show(message: CharSequence, neutral: OnClickListener) {
        dialog(getString(android.R.string.ok), message, null, null, neutral)
    }
}