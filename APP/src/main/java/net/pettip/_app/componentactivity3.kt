/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 8. 17.   description...
 */

package net.pettip._app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

/**
 * @Project     : carepet-android
 * @FileName    : componentactivity3.kt
 * @Date        : 2023. 08. 17.
 * @author      : isyuun@care-biz.co.kr
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