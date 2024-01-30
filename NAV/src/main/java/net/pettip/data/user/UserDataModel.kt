/*
 *  * Copyright(c) 2023 PetTip All right reserved.
 *  * This software is the proprietary information of PetTip.
 *  *
 *  * Revision History
 *  * Author                         Date             Description
 *  * --------------------------    -------------    ----------------------------------------
 *  * sanghun.lee@care-biz.co.kr     2023. 7. 28.    PetTip Development...
 *  *
 */

package net.pettip.data.user

data class UserDataModel(
    var appKey: String,
    var appOs: String,
    var appTypNm: String? = "",
    var ncknm: String,
    var snsLogin: String? = "",
    var userID: String,
    var userName: String,
    var userPW: String,
    var pushAdUseYn: String,
    var pushMdnghtUseYn: String,
    var pushUseYn: String,
)
