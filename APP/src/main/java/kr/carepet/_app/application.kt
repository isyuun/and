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
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 8. 21.   description...
 */

package kr.carepet._app

import android.app.Activity

/**
 * @Project     : carepet-android
 * @FileName    : application.kt
 * @Date        : 2023. 08. 21.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
open class application : android.app.Application() {
    private val __CLASSNAME__ = Exception().stackTrace[0].fileName

    private var activity: Activity? = null

    open fun getActivity(): Activity? {
        return activity
    }

    open fun setActivity(activity: Activity?) {
        this.activity = activity
    }
}