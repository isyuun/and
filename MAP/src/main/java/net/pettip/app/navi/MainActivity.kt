/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.27
 *
 */

package net.pettip.app.navi

import androidx.activity.compose.setContent
import net.pettip.map.app.naver.ShowDialogRestart
import net.pettip.ui.theme.APPTheme
import net.pettip.util.Log
import net.pettip.util.getMethodName

/**
 * @Project     : PetTip-Android
 * @FileName    : MainActivity
 * @Date        : 2023-12-27
 * @author      : isyuun
 * @description : net.pettip.app.navi
 * @see net.pettip.app.navi.MainActivity
 */
class MainActivity : net.pettip._test.app.navi.MainActivity() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    override fun setContent() {
        setContent {
            Log.wtf(__CLASSNAME__, "${getMethodName()}...")
            APPTheme {
                SetContent()
                ShowDialogRestart()
            }
        }
    }
}