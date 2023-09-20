/*
 *  Copyright 2011 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
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