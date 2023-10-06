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
import kr.carepet.data.daily.DailyDetailReq
import kr.carepet.data.daily.DailyDetailRes
import kr.carepet.data.daily.DailyMonthReq
import kr.carepet.data.daily.DailyMonthRes
import kr.carepet.data.daily.PhotoRes
import kr.carepet.data.daily.WalkListReq
import kr.carepet.data.daily.WalkListRes
import kr.carepet.data.daily.WeekRecordReq
import kr.carepet.data.daily.WeekRecordRes
import kr.carepet.data.pet.InviteCodeReq
import kr.carepet.data.pet.InviteCodeRes
import kr.carepet.data.pet.MyPetListReq
import kr.carepet.data.pet.MyPetListRes
import kr.carepet.data.pet.MyPetResModel
import kr.carepet.data.pet.PetListModel
import kr.carepet.data.pet.PetListResModel
import kr.carepet.data.pet.PetModel
import kr.carepet.data.pet.SetInviteCodeRes
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
        @Part("petRelCd") petRelCd: RequestBody,
        @Part("petNm") petNm: RequestBody,
        @Part("petRegNo") petRegNo: RequestBody,
        @Part("stdgSggCd") stdgSggCd: RequestBody,
        @Part("petInfoUnqNo") petInfoUnqNo: RequestBody,
        @Part("petBrthYmd") petBrthYmd: RequestBody,
        @Part("delYn") delYn: RequestBody,
        @Part("stdgUmdCd") stdgUmdCd: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("petRprsYn") petRprsYn: RequestBody,
        @Part("ntrTypCd") ntrTypCd: RequestBody,
        @Part("sexTypCd") sexTypCd: RequestBody,
        @Part("petMngrYn") petMngrYn: RequestBody,
        @Part("stdgCtpvCd") stdgCtpvCd: RequestBody,
        @Part("wghtVl") wghtVl: Float,
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

    @POST("api/v1/daily-life/week/recode")
    fun getWeekRecord(@Body data: WeekRecordReq) : Call<WeekRecordRes>

    @POST("api/v1/daily-life/list")
    fun getWalkList(@Body data: WalkListReq): Call<WalkListRes>

    @POST("api/v1/daily-life/view")
    fun getDailyDetail(@Body data: DailyDetailReq): Call<DailyDetailRes>

    @POST("api/v1/daily-life/month/recode")
    fun getMonthData(@Body data: DailyMonthReq): Call<DailyMonthRes>

    @POST("api/v1/mypet/invtt/create")
    fun getInviteCode(@Body data: InviteCodeReq): Call<InviteCodeRes>

    @POST("api/v1/mypet/invtt/setKey")
    fun setInviteCode(@Body data: String): Call<SetInviteCodeRes>


}