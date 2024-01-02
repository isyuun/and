/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.8
 *
 */

package net.pettip.map.app

import android.os.Bundle
import android.view.WindowManager
import net.pettip.map.app.naver.NaverGpxComponentActivity

/**
 * @Project     : PetTip-Android
 * @FileName    : GpxActivity
 * @Date        : 2023-12-08
 * @author      : isyuun
 * @description : net.pettip.map.app
 * @see net.pettip.map.app.GpxActivity
 */
open class GpxActivity : NaverGpxComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}