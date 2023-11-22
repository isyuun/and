/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 4.   description...
 */

package kr.carepet.app.navi

import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import kr.carepet.app.navi.ui.theme.AppTheme
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : MainActivity.kt
 * @Date        : 2023. 09. 04.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class MapActivity : kr.carepet.map.app.MapActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun setContent() {
        Log.v(__CLASSNAME__, "${getMethodName()}...")
        //super.setContent()
        setContent {
            AppTheme(dynamicColor = true) {
                Surface {
                    MapApp()
                }
            }
        }
    }
}