/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 10. 6.   description...
 */

package net.pettip.app.navi

import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import net.pettip.app.navi.ui.theme.AppTheme
import net.pettip.util.Log
import net.pettip.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : MapActivity.kt
 * @Date        : 2023. 10. 06.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
class MapActivity : net.pettip.map.app.MapActivity() {
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