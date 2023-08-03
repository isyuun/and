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

data class MyPetDataModel(
    var petBrthYmd: String,
    var petInfoUnqNo: Int,
    var petNm: String,
    var petRelCd: String,
    var petRprsYn: String,
    var stdgCtpvCd: String,
    var stdgEmdCd: String,
    var stdgSggCd: String
)
