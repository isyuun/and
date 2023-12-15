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

package net.pettip.app.navi

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import net.pettip.map.app.naver.GpxApp
import net.pettip.ui.theme.APPTheme
import net.pettip.util.Log
import net.pettip.util.getMethodName

/**
 * @Project     : PetTip-Android
 * @FileName    : GPXActivity
 * @Date        : 2023-12-08
 * @author      : isyuun
 * @description : net.pettip.app.navi
 * @see net.pettip.app.navi.GpxActivity
 */
class GpxActivity : net.pettip.map.app.GpxActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun setContent() {
        Log.v(__CLASSNAME__, "${getMethodName()}...")
        //super.setContent()
        setContent {
            APPTheme {
                Surface {
                    GpxApp()
                }
            }
        }
    }
}