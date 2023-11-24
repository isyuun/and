/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 21.   description...
 */

package net.pettip.map.app.naver

import com.naver.maps.map.NaverMapSdk
import net.pettip.gps.app.GPSApplication

/**
 * @Project     : carepet-android
 * @FileName    : NaverMapApplication.kt
 * @Date        : 2023. 09. 20.
 * @author      : isyuun@care-biz.co.kr
 * @description :
 */
const val NAVERMAP_PERMISSION_REQUEST_CODE = 100

open class NaverMapApplication : GPSApplication() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("4wgixyse4n")
    }
}