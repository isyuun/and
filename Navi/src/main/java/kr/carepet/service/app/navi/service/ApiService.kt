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

package kr.carepet.service.app.navi.service

import kr.carepet.service.app.navi.model.LoginData
import kr.carepet.service.app.navi.model.LoginResModel
import kr.carepet.service.app.navi.model.MyPetResModel
import kr.carepet.service.app.navi.model.PetModel
import kr.carepet.service.app.navi.model.RefreshRes
import kr.carepet.service.app.navi.model.UserDataModel
import kr.carepet.service.app.navi.model.UserDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/member/create-user")
    fun sendUserToServer(@Body data:UserDataModel):Call<UserDataResponse>

    @POST("api/v1/member/login")
    fun sendLoginToServer(@Body data:LoginData):Call<LoginResModel>

    @POST("api/v1/member/refresh-token")
    fun sendRefreshToken(@Body refreshToken:String):Call<RefreshRes>

    @POST("api/v1/member/refresh-token")
    fun sendPetDataToServer(@Body data:PetModel):Call<MyPetResModel>
}