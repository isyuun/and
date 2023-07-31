/*
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 7. 31.   description...
 */

package kr.carepet._app

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
    private lateinit var alertDialog: AlertDialog

    fun dialog(title: CharSequence, message: CharSequence, positive: OnClickListener? = null, negative: OnClickListener? = null, neutral: OnClickListener? = null) {
        alertDialog = this.let {
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
}