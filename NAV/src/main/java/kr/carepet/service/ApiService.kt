package kr.carepet.service

import kr.carepet.data.CommonCodeModel
import kr.carepet.data.CommonCodeResModel
import kr.carepet.data.RefreshRes
import kr.carepet.data.RefreshToken
import kr.carepet.data.SggListRes
import kr.carepet.data.UmdListReq
import kr.carepet.data.UmdListRes
import kr.carepet.data.cmm.NidUserInfoResponse
import kr.carepet.data.cmm.commonRes
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
import kr.carepet.data.pet.CurrentPetRes
import kr.carepet.data.pet.DeletePetReq
import kr.carepet.data.pet.InviteCodeReq
import kr.carepet.data.pet.InviteCodeRes
import kr.carepet.data.pet.MyPetListReq
import kr.carepet.data.pet.MyPetListRes
import kr.carepet.data.pet.MyPetResModel
import kr.carepet.data.pet.PetDetailReq
import kr.carepet.data.pet.PetDetailRes
import kr.carepet.data.pet.PetListModel
import kr.carepet.data.pet.PetListResModel
import kr.carepet.data.pet.PetModel
import kr.carepet.data.pet.SetInviteCodeRes
import kr.carepet.data.user.BbsReq
import kr.carepet.data.user.FAQRes
import kr.carepet.data.user.LoginData
import kr.carepet.data.user.LoginResModel
import kr.carepet.data.user.LogoutRes
import kr.carepet.data.user.NickNameCheckRes
import kr.carepet.data.user.QnaReq
import kr.carepet.data.user.QnaRes
import kr.carepet.data.user.RelCloseReq
import kr.carepet.data.user.ResetNickNameReq
import kr.carepet.data.user.ResetPwReq
import kr.carepet.data.user.UserDataModel
import kr.carepet.data.user.UserDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("/api/v1/member/chk-ncknm")
    fun nickNameCheck(@Body data: String): Call<NickNameCheckRes>

    @POST("api/v1/member/create-user")
    fun sendUserToServer(@Body data: UserDataModel): Call<UserDataResponse>

    @POST("api/v1/member/login")
    fun sendLoginToServer(@Body data: LoginData): Call<LoginResModel>

    @POST("api/v1/member/logOut")
    fun sendLogout(): Call<LogoutRes>

    @POST("/api/v1/member/withdraw")
    fun withdraw(): Call<commonRes>

    @POST("/api/v1/mypet/rel-close")
    fun relClose(@Body data : RelCloseReq): Call<commonRes>

    @POST("api/v1/member/refresh-token")
    fun sendRefreshToken(@Body refreshToken: RefreshToken): Call<RefreshRes>

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

    @Multipart
    @POST("api/v1/mypet/update")
    fun modifyPet(
        @Part("ownrPetUnqNo") ownrPetUnqNo: RequestBody,
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
    ):Call<MyPetResModel>

    @POST("api/v1/mypet/list")
    fun myPetList(@Body data: MyPetListReq) : Call<MyPetListRes>

    @POST("/api/v1/mypet/detail")
    fun myPetDetail(@Body data: PetDetailReq) : Call<PetDetailRes>

    @POST("/api/v1/daily-life/pet/list")
    fun myPetListCurrent(@Body data: MyPetListReq) : Call<CurrentPetRes>

    @Multipart
    @POST("api/v1/daily-life/upload")
    fun uploadPhoto(@Part files: ArrayList<MultipartBody.Part>): Call<PhotoRes>

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

    @GET("/v1/nid/me")
    fun getNidUserInfo(@Header("Authorization") authorization:String): Call<NidUserInfoResponse>

    @POST("api/v1/member/reset-ncknm")
    fun resetNickName(@Body data: ResetNickNameReq): Call<commonRes>

    @POST("api/v1/member/reset-password")
    fun resetPw(@Body data : ResetPwReq) : Call<commonRes>

    @POST("api/v1/mypet/delete")
    fun deletePet(@Body data : DeletePetReq) : Call<commonRes>

    // 게시판
    @POST("api/v1/bbs/faq/list")
    fun getFaqList(@Body data : BbsReq) : Call<FAQRes>

    @POST("api/v1/bbs/qna/bsc/list")
    fun getQnaList(@Body data : QnaReq) : Call<QnaRes>

}