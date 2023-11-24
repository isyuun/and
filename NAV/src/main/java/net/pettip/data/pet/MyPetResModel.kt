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

package net.pettip.data.pet

data class MyPetResModel(
    val data: Any?,
    val statusCode: Int,
    val resultMessage: String?,
    val detailMessage: String?
)
