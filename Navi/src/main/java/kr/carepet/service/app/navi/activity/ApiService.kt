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

package kr.carepet.service.app.navi.activity

import kr.carepet.service.app.navi.model.UserDataModel
import kr.carepet.service.app.navi.model.UserDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/member/create-user")
    fun sendUserToServer(@Body data:UserDataModel):Call<UserDataResponse>

    //@POST("api/v1/mypet/create-pet")
    //fun sendMyPetToServer(@Body data:)
}