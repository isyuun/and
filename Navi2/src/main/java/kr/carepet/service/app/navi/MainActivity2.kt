/*
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of Kyobo Book.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 7. 28.   description...
 */

package kr.carepet.service.app.navi

import android.os.Bundle
import kr.carepet.util.Log

/**
 * @Project     : carepet-android
 * @FileName    : MainActivity2.kt
 * @Date        : 2023. 07. 28.
 * @author      : isyuun@care-pet.kr
 * @description : 카피라잇/파일주석/메서드주석
 */
open class MainActivity2 : MainActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(__CLASSNAME__, "${getMethodName()}")
        super.onCreate(savedInstanceState)
        //setContentView(0)       //IY:test-일부러.죽여봄
    }
}