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
 *  isyuun@care-pet.kr             2023. 9. 18.   description...
 */

package kr.carepet.gps

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import kr.carepet._gps.foregroundonlylocationservice3

/**
 * @Project     : carepet-android
 * @FileName    : CameraContentObserver.kt
 * @Date        : 2023. 09. 18.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
class CameraContentObserver(private val context: foregroundonlylocationservice3, handler: Handler) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        context.onChange(selfChange, uri)
    }
}