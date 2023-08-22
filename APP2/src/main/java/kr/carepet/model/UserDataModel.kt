/*
 *  * Copyright(c) 2023 CarePat All right reserved.
 *  * This software is the proprietary information of CarePet.
 *  *
 *  * Revision History
 *  * Author                         Date             Description
 *  * --------------------------    -------------    ----------------------------------------
 *  * sanghun.lee@care-biz.co.kr     2023. 7. 28.    CarePet Development...
 *  *
 */

package kr.carepet.model

data class UserDataModel(
    var appKey: String,
    var appOs: String,
    var appTypNm: String? = "",
    var snsLogin: String? = "",
    var userID: String,
    var userName: String,
    var userPW: String
)
