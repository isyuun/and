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

package kr.carepet.service

import kr.carepet.data.CommonCodeModel
import kr.carepet.data.CommonCodeResModel
import kr.carepet.data.RefreshRes
import kr.carepet.data.RefreshToken
import kr.carepet.data.SggListRes
import kr.carepet.data.UmdListReq
import kr.carepet.data.UmdListRes
import kr.carepet.data.daily.DailyCreateReq
import kr.carepet.data.daily.DailyCreateRes
import kr.carepet.data.daily.PhotoRes
import kr.carepet.data.pet.MyPetListReq
import kr.carepet.data.pet.MyPetListRes
import kr.carepet.data.pet.MyPetResModel
import kr.carepet.data.pet.PetListModel
import kr.carepet.data.pet.PetListResModel
import kr.carepet.data.pet.PetModel
import kr.carepet.data.user.LoginData
import kr.carepet.data.user.LoginResModel
import kr.carepet.data.user.LogoutRes
import kr.carepet.data.user.UserDataModel
import kr.carepet.data.user.UserDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("api/v1/member/create-user")
    fun sendUserToServer(@Body data: UserDataModel): Call<UserDataResponse>

    @POST("api/v1/member/login")
    fun sendLoginToServer(@Body data: LoginData): Call<LoginResModel>

    @POST("api/v1/member/logOut")
    fun sendLogout(): Call<LogoutRes>

    @POST("api/v1/member/refresh-token")
    fun sendRefreshToken(@Body refreshToken: RefreshToken): Call<RefreshRes>

    @POST("api/v1/member/refresh-token")
    fun sendPetDataToServer(@Body data: PetModel): Call<MyPetResModel>

    @POST("api/v1/cmm/code-list")
    fun commonCodeList(@Body data: CommonCodeModel) : Call<CommonCodeResModel>

    @POST("api/v1/cmm/pet-list")
    fun petList(@Body data: PetListModel) : Call<PetListResModel>

    @Multipart
    @POST("api/v1/mypet/create")
    fun createPet(
        @Part("ntrYn") ntrYn: RequestBody,
        @Part("petRelCd") petRelCd: RequestBody,
        @Part("petNm") petNm: RequestBody,
        @Part("petRegNo") petRegNo: RequestBody,
        @Part("stdgSggCd") stdgSggCd: RequestBody,
        @Part("petInfoUnqNo") petInfoUnqNo: RequestBody,
        @Part("petBrthYmd") petBrthYmd: RequestBody,
        @Part("stdgUmdCd") stdgUmdCd: RequestBody,
        @Part("delYn") delYn: RequestBody,
        @Part file :MultipartBody.Part,
        @Part("petRprsYn") petRprsYn: RequestBody,
        @Part("sexTypCd") sexTypCd: RequestBody,
        @Part("petMngrYn") petMngrYn: RequestBody,
        @Part("wghtVl") wghtVl: Float,
        @Part("stdgCtpvCd") stdgCtpvCd: RequestBody
    ):Call<MyPetResModel>

    @POST("api/v1/mypet/list")
    fun myPetList(@Body data: MyPetListReq) : Call<MyPetListRes>

    @Multipart
    @POST("api/v1/daily-life/upload")
    fun uploadPhoto(@Part photos: List<MultipartBody.Part>): Call<PhotoRes>

    @POST("api/v1/daily-life/create")
    fun uploadDaily(@Body data: DailyCreateReq): Call<DailyCreateRes>

    @POST("api/v1/cmm/sgg-list")
    fun getSggList(@Body data: String): Call<SggListRes>

    @POST("api/v1/cmm/umd-list")
    fun getUmdList(@Body data: UmdListReq): Call<UmdListRes>
}