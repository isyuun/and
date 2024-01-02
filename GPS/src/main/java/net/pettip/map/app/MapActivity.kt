/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 20.   description...
 */

package net.pettip.map.app

import android.os.Bundle
import android.view.WindowManager
import net.pettip.map.app.naver.NaverMapComponentActivity

/**
 * @Project     : carepet-android
 * @FileName    : MapActivity.kt
 * @Date        : 2023. 09. 20.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
open class MapActivity : NaverMapComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}