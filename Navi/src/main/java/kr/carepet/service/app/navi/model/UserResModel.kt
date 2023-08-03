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

package kr.carepet.service.app.navi.model

data class UserDataResponse(
    val data: UserData?,
    val statusCode: Int?,
    val resultMessage: String?,
    val detailMessage: String?
)

data class UserData(
    val status: Boolean?,
    val message: String?,
    val failReason: String?,
    val userID: String?
)
