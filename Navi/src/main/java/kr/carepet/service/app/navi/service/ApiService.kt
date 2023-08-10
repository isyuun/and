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

import kr.carepet.model.LoginData
import kr.carepet.model.LoginResModel
import kr.carepet.model.MyPetResModel
import kr.carepet.model.PetModel
import kr.carepet.model.RefreshRes
import kr.carepet.model.RefreshToken
import kr.carepet.model.UserDataModel
import kr.carepet.model.UserDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/member/create-user")
    fun sendUserToServer(@Body data:UserDataModel):Call<UserDataResponse>

    @POST("api/v1/member/login")
    fun sendLoginToServer(@Body data:LoginData):Call<LoginResModel>

    @POST("api/v1/member/refresh-token")
    fun sendRefreshToken(@Body refreshToken:RefreshToken):Call<RefreshRes>

    @POST("api/v1/member/refresh-token")
    fun sendPetDataToServer(@Body data:PetModel):Call<MyPetResModel>
}