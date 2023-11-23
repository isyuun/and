package kr.carepet.service

import kr.carepet.data.CommonCodeModel
import kr.carepet.data.CommonCodeResModel
import kr.carepet.data.RefreshRes
import kr.carepet.data.RefreshToken
import kr.carepet.data.SggListRes
import kr.carepet.data.UmdListReq
import kr.carepet.data.UmdListRes
import kr.carepet.data.bbs.BbsCmntCreateRes
import kr.carepet.data.bbs.BbsCmntRcmdtnReq
import kr.carepet.data.bbs.BbsCmntUpdateReq
import kr.carepet.data.bbs.BbsCmtCreateReq
import kr.carepet.data.bbs.EventDetailRes
import kr.carepet.data.bbs.EventListRes
import kr.carepet.data.cmm.CmmRes
import kr.carepet.data.cmm.CommonData
import kr.carepet.data.cmm.WeatherReq
import kr.carepet.data.cmm.WeatherRes
import kr.carepet.data.cmm.commonRes
import kr.carepet.data.daily.BbsCmntDeleteReq
import kr.carepet.data.daily.CmntCreateReq
import kr.carepet.data.daily.CmntCreateRes
import kr.carepet.data.daily.CmntDeleteReq
import kr.carepet.data.daily.CmntRcmdtnReq
import kr.carepet.data.daily.CmntUpdateReq
import kr.carepet.data.daily.DailyCreateReq
import kr.carepet.data.daily.DailyCreateRes
import kr.carepet.data.daily.DailyDetailData
import kr.carepet.data.daily.DailyDetailReq
import kr.carepet.data.daily.DailyDetailRes
import kr.carepet.data.daily.DailyMonthReq
import kr.carepet.data.daily.DailyMonthRes
import kr.carepet.data.daily.DailyRcmdtn
import kr.carepet.data.daily.DailyUpdateReq
import kr.carepet.data.daily.PhotoRes
import kr.carepet.data.daily.RTStoryListRes
import kr.carepet.data.daily.RlsDailyReq
import kr.carepet.data.daily.StoryReq
import kr.carepet.data.daily.StoryRes
import kr.carepet.data.daily.WalkListReq
import kr.carepet.data.daily.WalkListRes
import kr.carepet.data.daily.WeekRecordReq
import kr.carepet.data.daily.WeekRecordRes
import kr.carepet.data.pet.ChangePetWgtReq
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
import kr.carepet.data.pet.PetWgtRes
import kr.carepet.data.pet.RegPetWgtReq
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
import kr.carepet.data.user.TrmnlMngReq
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

    // --------------------- 게시판 ----------------------- //
    @POST("api/v1/bbs/faq/list")
    fun getFaqList(@Body data: BbsReq): Call<FAQRes>

    @POST("api/v1/bbs/qna/bsc/list")
    fun getQnaList(@Body data: QnaReq): Call<QnaRes>

    @POST("api/v1/bbs/event/list")
    fun getEventList(@Body data: BbsReq): Call<EventListRes>

    @POST("api/v1/bbs/event/dtl/list")
    fun getEventDetail(@Body data: Int): Call<EventDetailRes>

    @POST("api/v1/bbs/cmnt/delete")
    fun bbsCmntDelete(@Body data: BbsCmntDeleteReq): Call<BbsCmntCreateRes>

    @POST("api/v1/bbs/cmnt/create")
    fun bbsCmntCreate(@Body data: BbsCmtCreateReq): Call<BbsCmntCreateRes>

    @POST("api/v1/bbs/cmnt/rcmdtn")
    fun bbsCmntRcmdtn(@Body data: BbsCmntRcmdtnReq): Call<BbsCmntCreateRes>

    @POST("api/v1/bbs/cmnt/update")
    fun bbsCmntUpdate(@Body data: BbsCmntUpdateReq): Call<BbsCmntCreateRes>
    // --------------------- 게시판 ----------------------- //


    // --------------------- 회원 ----------------------- //
    @POST("/api/v1/member/chk-ncknm")
    fun nickNameCheck(@Body data: String): Call<NickNameCheckRes>

    @POST("api/v1/member/create-user")
    fun sendUserToServer(@Body data: UserDataModel): Call<UserDataResponse>

    @POST("api/v1/member/logOut")
    fun sendLogout(): Call<LogoutRes>

    @POST("api/v1/member/login")
    fun sendLoginToServer(@Body data: LoginData): Call<LoginResModel>

    @POST("api/v1/member/refresh-token")
    fun sendRefreshToken(@Body refreshToken: RefreshToken): Call<RefreshRes>

    @POST("api/v1/member/reset-ncknm")
    fun resetNickName(@Body data: ResetNickNameReq): Call<commonRes>

    @POST("api/v1/member/reset-password")
    fun resetPw(@Body data: ResetPwReq): Call<commonRes>

    @POST("/api/v1/member/withdraw")
    fun withdraw(): Call<commonRes>

    @POST("/api/v1/member/trmnlMng")
    fun trmnlMng(@Body data:TrmnlMngReq): Call<commonRes>
    // --------------------- 회원 ----------------------- //

    // --------------------- 일상생활 ----------------------- //

    @POST("api/v1/daily-life/create")
    fun uploadDaily(@Body data: DailyCreateReq): Call<DailyCreateRes>

    @POST("api/v1/daily-life/rcmdtn")
    fun rcmdtnDaily(@Body data: DailyRcmdtn): Call<DailyDetailRes>

    @POST("api/v1/daily-life/update")
    fun updateDaily(@Body data: DailyUpdateReq): Call<DailyDetailRes>

    @POST("api/v1/daily-life/rls/update")
    fun rlsDaily(@Body data: RlsDailyReq): Call<DailyDetailRes>

    @POST("api/v1/daily-life/list")
    fun getWalkList(@Body data: WalkListReq): Call<WalkListRes>

    @POST("api/v1/daily-life/month/recode")
    fun getMonthData(@Body data: DailyMonthReq): Call<DailyMonthRes>

    @POST("/api/v1/daily-life/pet/list")
    fun myPetListCurrent(@Body data: MyPetListReq): Call<CurrentPetRes>

    @Multipart
    @POST("api/v1/daily-life/upload")
    fun uploadPhoto(@Part files: ArrayList<MultipartBody.Part>): Call<PhotoRes>

    @POST("api/v1/daily-life/view")
    fun getDailyDetail(@Body data: DailyDetailReq): Call<DailyDetailRes>

    @POST("api/v1/daily-life/week/recode")
    fun getWeekRecord(@Body data: WeekRecordReq): Call<WeekRecordRes>

    @POST("api/v1/daily-life/cmnt/create")
    fun cmntCreate(@Body data: CmntCreateReq): Call<CmntCreateRes>

    @POST("api/v1/daily-life/cmnt/delete")
    fun cmntDelete(@Body data: CmntDeleteReq): Call<CmntCreateRes>

    @POST("api/v1/daily-life/cmnt/update")
    fun cmntUpdate(@Body data: CmntUpdateReq): Call<CmntCreateRes>

    @POST("api/v1/daily-life/cmnt/rcmdtn")
    fun cmntRcmdtn(@Body data: CmntRcmdtnReq): Call<CmntCreateRes>

    // --------------------- 일상생활 ----------------------- //

    // --------------------- 마이펫 ----------------------- //
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
    ): Call<MyPetResModel>

    @POST("api/v1/mypet/delete")
    fun deletePet(@Body data: DeletePetReq): Call<commonRes>

    @POST("/api/v1/mypet/detail")
    fun myPetDetail(@Body data: PetDetailReq): Call<PetDetailRes>

    @POST("api/v1/mypet/invtt/create")
    fun getInviteCode(@Body data: InviteCodeReq): Call<InviteCodeRes>

    @POST("api/v1/mypet/invtt/setKey")
    fun setInviteCode(@Body data: String): Call<SetInviteCodeRes>

    @POST("api/v1/mypet/list")
    fun myPetList(@Body data: MyPetListReq): Call<MyPetListRes>

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
    ): Call<MyPetResModel>

    @POST("/api/v1/mypet/rel-close")
    fun relClose(@Body data: RelCloseReq): Call<commonRes>

    @POST("/api/v1/mypet/wght/create")
    fun regPetWgt(@Body data: RegPetWgtReq): Call<commonRes>

    @POST("/api/v1/mypet/wght/delete")
    fun deletePetWgt(@Body data: Int): Call<commonRes>

    @POST("/api/v1/mypet/wght/list")
    fun getPetWgt(@Body data: String): Call<PetWgtRes>

    @POST("/api/v1/mypet/wght/update")
    fun changePetWgt(@Body data: ChangePetWgtReq): Call<commonRes>
    // --------------------- 마이펫 ----------------------- //

    // --------------------- 공통 코드 ----------------------- //
    @POST("api/v1/cmm/code-list")
    fun commonCodeList(@Body data: CommonCodeModel): Call<CommonCodeResModel>

    @POST("api/v1/cmm/code-list")
    fun getCmmList(@Body data: CommonCodeModel): Call<CmmRes>

    @POST("api/v1/cmm/pet-list")
    fun petList(@Body data: PetListModel): Call<PetListResModel>

    @POST("api/v1/cmm/sgg-list")
    fun getSggList(@Body data: String): Call<SggListRes>

    @POST("api/v1/cmm/umd-list")
    fun getUmdList(@Body data: UmdListReq): Call<UmdListRes>

    @POST("api/v1/weather")
    fun getWeather(@Body data: WeatherReq): Call<WeatherRes>
    // --------------------- 공통 코드 ----------------------- //

    // --------------------- 공통 코드 ----------------------- //
    @POST("api/v1/story/real-time-list")
    fun getRealTimeList(): Call<RTStoryListRes>

    @POST("/api/v1/story/list")
    fun getStoryList(@Body data: StoryReq): Call<StoryRes>

    // --------------------- 공통 코드 ----------------------- //
}