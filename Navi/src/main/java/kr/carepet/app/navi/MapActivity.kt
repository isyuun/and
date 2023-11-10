/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 10. 6.   description...
 */

package kr.carepet.app.navi

import androidx.activity.compose.setContent
import kr.carepet.app.navi.ui.theme.AppTheme
import kr.carepet.util.Log
import kr.carepet.util.getMethodName

/**
 * @Project     : carepet-android
 * @FileName    : MapActivity.kt
 * @Date        : 2023. 10. 06.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
class MapActivity : kr.carepet.map.app.MapActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    //private val fusedLocationSource: FusedLocationSource by lazy {
    //    FusedLocationSource(this, NAVERMAP_PERMISSION_REQUEST_CODE)
    //}

    override fun setContent() {
        Log.v(__CLASSNAME__, "${getMethodName()}...")
        //super.setContent()
        setContent {
            AppTheme {
                MapApp()
            }
        }
    }
}