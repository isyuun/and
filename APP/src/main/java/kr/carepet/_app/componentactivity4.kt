/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 8. 17.   description...
 */

package kr.carepet._app

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

/**
 * @Project     : carepet-android
 * @FileName    : componentactivity4.kt
 * @Date        : 2023. 08. 17.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class componentactivity4 : componentactivity3() {
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

            // Set other dialog propertiesp
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