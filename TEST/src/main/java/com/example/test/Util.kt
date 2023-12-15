/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.15
 *
 */

package com.example.test

import android.content.Context
import android.media.AudioManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * @Project     : PetTip-Android
 * @FileName    : Util
 * @Date        : 12/15/2023
 * @author      : isyuun
 * @description : com.example.test
 * @see com.example.test.Util
 */

/**
 * How to play the platform CLICK sound in Jetpack Compose Button click
 * @see <a href="https://stackoverflow.com/questions/66080018/how-to-play-the-platform-click-sound-in-jetpack-compose-button-click">How to play the platform CLICK sound in Jetpack Compose Button click</a>
 */
fun withClick(context: Context, onClick: () -> Unit): () -> Unit {
    return {
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).playSoundEffect(AudioManager.FX_KEY_CLICK)
        onClick()
    }
}

/**
 * How to play the platform CLICK sound in Jetpack Compose Button click
 * @see <a href="https://stackoverflow.com/questions/66080018/how-to-play-the-platform-click-sound-in-jetpack-compose-button-click">How to play the platform CLICK sound in Jetpack Compose Button click</a>
 */
@Composable
fun withClick(onClick: () -> Unit): () -> Unit {
    val context = LocalContext.current
    return {
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).playSoundEffect(AudioManager.FX_KEY_CLICK)
        onClick()
    }
}
