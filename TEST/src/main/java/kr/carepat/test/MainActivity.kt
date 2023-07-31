/*
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 7. 31.   description...
 */

package kr.carepat.test

import android.os.Bundle
import kr.carepet.app.AppCompatActivity
import kr.carepet.util.Log

open class MainActivity : AppCompatActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(__CLASSNAME__, "${getMethodName()}")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}