/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 20.   description...
 */

package kr.carepet.gps.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * @Project     : carepet-android
 * @FileName    : ForegroundOnlyBroadcastReceiver.kt
 * @Date        : 2023. 09. 08.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
/**
 * Receiver for location broadcasts from [ForegroundOnlyLocationService].
 *
 * IY: 앱이 활성화 상태일때 로컬 브로드케스트 메시지 전달확인.
 */
internal class ForegroundOnlyBroadcastReceiver2(private val iinterface: IForegroundOnlyBroadcastReceiver) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        iinterface.onReceive(context, intent)
    }
}

interface IForegroundOnlyBroadcastReceiver {
    fun onReceive(context: Context, intent: Intent)
}