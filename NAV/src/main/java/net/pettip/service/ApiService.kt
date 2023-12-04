package net.pettip.service

import net.pettip.data.CommonCodeModel
import net.pettip.data.CommonCodeResModel
import net.pettip.data.RefreshRes
import net.pettip.data.RefreshToken
import net.pettip.data.SggListRes
import net.pettip.data.UmdListReq
import net.pettip.data.UmdListRes
import net.pettip.data.bbs.BbsCmntCreateRes
import net.pettip.data.bbs.BbsCmntRcmdtnReq
import net.pettip.data.bbs.BbsCmntUpdateReq
import net.pettip.data.bbs.BbsCmtCreateReq
import net.pettip.data.bbs.BbsDetailRes
import net.pettip.data.bbs.BbsRcmdtnReq
import net.pettip.data.bbs.EndEventListRes
import net.pettip.data.bbs.EventDetailRes
import net.pettip.data.bbs.EventListRes
import net.pettip.data.bbs.NtcListRes
import net.pettip.data.bbs.QnaDetailRes
import net.pettip.data.bbs.QnaListRes
import net.pettip.data.bbs.QnaReq
import net.pettip.data.bbs.QnaUpdateReq
import net.pettip.data.cmm.CmmRes
import net.pettip.data.cmm.WeatherReq
import net.pettip.data.cmm.WeatherRes
import net.pettip.data.cmm.commonRes
import net.pettip.data.daily.BbsCmntDeleteReq
import net.pettip.data.daily.CmntCreateReq
import net.pettip.data.daily.CmntCreateRes
import net.pettip.data.daily.CmntDeleteReq
import net.pettip.data.daily.CmntRcmdtnReq
import net.pettip.data.daily.CmntUpdateReq
import net.pettip.data.daily.DailyCreateReq
import net.pettip.data.daily.DailyCreateRes
import net.pettip.data.daily.DailyDetailReq
import net.pettip.data.daily.DailyDetailRes
import net.pettip.data.daily.DailyMonthReq
import net.pettip.data.daily.DailyMonthRes
import net.pettip.data.daily.DailyRcmdtn
import net.pettip.data.daily.DailyRlsYnReq
import net.pettip.data.daily.DailyUpdateReq
import net.pettip.data.daily.PhotoRes
import net.pettip.data.daily.RTStoryListRes
import net.pettip.data.daily.RlsDailyReq
import net.pettip.data.daily.StoryReq
import net.pettip.data.daily.StoryRes
import net.pettip.data.daily.WalkListReq
import net.pettip.data.daily.WalkListRes
import net.pettip.data.daily.WeekRecordReq
import net.pettip.data.daily.WeekRecordRes
import net.pettip.data.pet.ChangePetWgtReq
import net.pettip.data.pet.CurrentPetRes
import net.pettip.data.pet.DeletePetReq
import net.pettip.data.pet.InviteCodeReq
import net.pettip.data.pet.InviteCodeRes
import net.pettip.data.pet.MyPetListReq
import net.pettip.data.pet.MyPetListRes
import net.pettip.data.pet.MyPetResModel
import net.pettip.data.pet.PetDetailReq
import net.pettip.data.pet.PetDetailRes
import net.pettip.data.pet.PetListModel
import net.pettip.data.pet.PetListResModel
import net.pettip.data.pet.PetWgtRes
import net.pettip.data.pet.RegPetWgtReq
import net.pettip.data.pet.SetInviteCodeRes
import net.pettip.data.user.BbsReq
import net.pettip.data.user.FAQRes
import net.pettip.data.user.LoginData
import net.pettip.data.user.LoginResModel
import net.pettip.data.user.LogoutRes
import net.pettip.data.user.NickNameCheckRes
import net.pettip.data.user.RelCloseReq
import net.pettip.data.user.ResetNickNameReq
import net.pettip.data.user.ResetPwReq
import net.pettip.data.user.TrmnlMngReq
import net.pettip.data.user.UserDataModel
import net.pettip.data.user.UserDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    // --------------------- 게시판 ----------------------- //

    @POST("api/v1/bbs/qna/create")
    fun createQna(@Body data:QnaReq): Call<BbsDetailRes>

    @POST("api/v1/bbs/faq/list")
    fun getFaqList(@Body data: BbsReq): Call<FAQRes>

    @POST("api/v1/bbs/qna/bsc/list")
    fun getQnaList(@Body data: BbsReq): Call<QnaListRes>

    @POST("api/v1/bbs/qna/dtl/list")
    fun getQnaDetail(@Body data: Int): Call<QnaDetailRes>

    @POST("api/v1/bbs/event/list")
    fun getEventList(@Body data: BbsReq): Call<EventListRes>

    @POST("api/v1/bbs/event/dtl/list")
    fun getEventDetail(@Body data: Int): Call<BbsDetailRes>

    @POST("api/v1/bbs/ancmntWinner/list")
    fun getEndEventList(@Body data: BbsReq): Call<EndEventListRes>

    @POST("api/v1/bbs/ancmntWinner/dtl/list")
    fun getEndEventDetail(@Body data: Int): Call<BbsDetailRes>

    @POST("api/v1/bbs/ntc/list")
    fun getNtcList(@Body data: BbsReq): Call<NtcListRes>

    @POST("api/v1/bbs/ntc/dtl/list")
    fun getNtcDetail(@Body data: Int): Call<BbsDetailRes>

    @POST("api/v1/bbs/cmnt/delete")
    fun bbsCmntDelete(@Body data: BbsCmntDeleteReq): Call<BbsCmntCreateRes>

    @POST("api/v1/bbs/cmnt/create")
    fun bbsCmntCreate(@Body data: BbsCmtCreateReq): Call<BbsCmntCreateRes>

    @POST("api/v1/bbs/cmnt/rcmdtn")
    fun bbsCmntRcmdtn(@Body data: BbsCmntRcmdtnReq): Call<BbsCmntCreateRes>

    @POST("api/v1/bbs/cmnt/update")
    fun bbsCmntUpdate(@Body data: BbsCmntUpdateReq): Call<BbsCmntCreateRes>

    @POST("api/v1/bbs/qna/delete")
    fun deleteQna(@Body data: Int): Call<CmmRes>

    @POST("api/v1/bbs/qna/update")
    fun updateQna(@Body data: QnaUpdateReq): Call<QnaDetailRes>

    @POST("api/v1/bbs/rcmdtn")
    fun bbsRcmdtn(@Body data: BbsRcmdtnReq): Call<BbsDetailRes>
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

    @Multipart
    @POST("api/v1/bbs/qna/atch/upload")
    fun uploadPhotoInBbs(@Part files: ArrayList<MultipartBody.Part>): Call<PhotoRes>

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

    @POST("api/v1/daily-life/rls/update")
    fun dailyRlsUpdate(@Body data: DailyRlsYnReq): Call<DailyDetailRes>

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