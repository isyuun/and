package net.pettip.app.navi.viewmodel

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import net.pettip.data.RefreshRes
import net.pettip.data.RefreshToken
import net.pettip.data.cmm.commonRes
import net.pettip.data.daily.WeekData
import net.pettip.data.daily.WeekRecordReq
import net.pettip.data.daily.WeekRecordRes
import net.pettip.data.pet.CurrentPetData
import net.pettip.data.pet.CurrentPetRes
import net.pettip.data.pet.MyPetListReq
import net.pettip.data.pet.MyPetListRes
import net.pettip.data.pet.PetDetailData
import net.pettip.data.user.TrmnlMngReq
import net.pettip.singleton.G
import net.pettip.singleton.MySharedPreference
import net.pettip.singleton.RetrofitClientServer
import net.pettip.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume


class SharedViewModel:ViewModel(){

    val emptyPet = PetDetailData(
        ownrPetUnqNo = "",
        petBrthYmd = "미상",
        petInfoUnqNo = 0,
        petKindNm = "웨스트 하이랜드 화이트 테리어",
        petMngrYn = "",
        petNm = "배추",
        petRegNo = "",
        petRelCd = "",
        petRelNm = "",
        petRelUnqNo = 0,
        petRprsImgAddr = "",
        petRprsYn = "Y",
        sexTypCd = "",
        sexTypNm = "모름",
        stdgCtpvCd = "",
        stdgCtpvNm = "",
        stdgSggCd = "",
        stdgSggNm = "",
        stdgUmdCd = "",
        stdgUmdNm = "",
        wghtVl = 0.0f,
        ntrTypCd = "",
        ntrTypNm = "모름",
        endDt = "",
        mngrType = "M",
        memberList = emptyList(),
        petTypCd = null
    )

    private val _inviteCode = MutableStateFlow<String?>(null)
    val inviteCode:StateFlow<String?> = _inviteCode.asStateFlow()
    fun updateInviteCode(newValue: String?){_inviteCode.value = newValue}

    private val _currentTab = MutableStateFlow<String>("스토리")
    val currentTab:StateFlow<String> = _currentTab.asStateFlow()
    fun updateCurrentTab(newValue: String){ _currentTab.value = newValue}

    private val _profilePet = MutableStateFlow<PetDetailData>(emptyPet)
    val profilePet:StateFlow<PetDetailData> = _profilePet.asStateFlow()
    fun updateProfilePet(newValue: PetDetailData){ _profilePet.value = newValue}

    private val _pushData = MutableStateFlow<Bundle?>(null)
    val pushData:StateFlow<Bundle?> = _pushData.asStateFlow()
    fun updatePushData(newValue: Bundle?){ _pushData.value = newValue}

    private val _nickName = MutableStateFlow<String>("")
    val nickName:StateFlow<String> = _nickName.asStateFlow()
    fun updateNickName(newValue: String){ _nickName.value = newValue}

    private val _walkUpload = MutableStateFlow<Boolean>(false) // 산책 업로드시 true
    val walkUpload:StateFlow<Boolean> = _walkUpload.asStateFlow()
    fun updateWalkUpload(newValue: Boolean){ _walkUpload.value = newValue }

    private val _init = MutableStateFlow<Boolean>(true)
    val init:StateFlow<Boolean> = _init.asStateFlow()
    fun updateInit(newValue: Boolean){ _init.value = newValue }

    private val _dupleLogin = MutableStateFlow<Boolean>(false)
    val dupleLogin:StateFlow<Boolean> = _dupleLogin.asStateFlow()
    fun updateDupleLogin(newValue: Boolean){
        _dupleLogin.value = newValue
    }

    private val _moreStoryClick = MutableStateFlow<Int?>(null)
    val moreStoryClick:StateFlow<Int?> = _moreStoryClick.asStateFlow()
    fun updateMoreStoryClick(newValue: Int?){
        _moreStoryClick.value = newValue
    }

    private val _toStory = MutableStateFlow<Boolean>(false)
    val toStory:StateFlow<Boolean> = _toStory.asStateFlow()
    fun updateToStory(newValue: Boolean){
        _toStory.value = newValue
    }

    private val _weekRecord = MutableStateFlow<WeekData?>(null)
    val weekRecord: StateFlow<WeekData?> = _weekRecord.asStateFlow()
    fun updateWeekRecord(newData: WeekData) {
        _weekRecord.value = newData
    }

    private var _petInfo = MutableStateFlow<List<PetDetailData>>(emptyList())
    var petInfo: StateFlow<List<PetDetailData>> = _petInfo.asStateFlow()
    fun updatePetInfo(newData: List<PetDetailData>) {
        _petInfo.value = newData
    }

    private var _currentPetInfo = MutableStateFlow<List<CurrentPetData>>(emptyList())
    var currentPetInfo: StateFlow<List<CurrentPetData>> = _currentPetInfo.asStateFlow()
    fun updateCurrentPetInfo(newData: List<CurrentPetData>) {
        _currentPetInfo.value = newData
    }

    private val _selectPet = MutableStateFlow<CurrentPetData?>(null)
    val selectPet: StateFlow<CurrentPetData?> = _selectPet.asStateFlow()
    fun updateSelectPet(newValue: CurrentPetData?) { _selectPet.value = newValue }

    private val _selectPetTemp = MutableStateFlow<CurrentPetData?>(null)
    val selectPetTemp: StateFlow<CurrentPetData?> = _selectPetTemp.asStateFlow()
    fun updateSelectPetTemp(newValue: CurrentPetData) { _selectPetTemp.value = newValue }

    private val _selectPetMulti = MutableStateFlow<MutableList<CurrentPetData>>(mutableListOf())
    val selectPetMulti: StateFlow<List<CurrentPetData>> = _selectPetMulti.asStateFlow()
    fun addSelectPetMulti(newValue: CurrentPetData) { _selectPetMulti.value.add(newValue) }
    fun subSelectPetMulti(newValue: CurrentPetData) { _selectPetMulti.value.remove(newValue) }

    fun initSelectPet(){
        if (_currentPetInfo.value.isNotEmpty()){
            _selectPet.value = _currentPetInfo.value[0]
        }else{
            _selectPet.value = null
        }
    }

    fun parseBirthday(birthdayString: String): LocalDate? {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return try {
            LocalDate.parse(birthdayString, formatter)
        } catch (e: Exception) {
            null // 파싱에 실패하면 null 반환 또는 에러 처리
        }
    }

    fun changeBirth(birth: String): String {
        val birthday = parseBirthday(birth)
        val currentDate = LocalDate.now() // 현재 날짜

        val period = Period.between(birthday, currentDate) // 두 날짜 간의 기간 계산

        val years = period.years // 연 차이
        val months = period.months // 월 차이
        val days = period.days // 일 차이

        val yearStr = if (years > 0) {
            "${years}년"
        } else {
            ""
        }

        val monthStr = if (months > 0) {
            "${months}개월"
        } else {
            ""
        }

        val dayStr = if (days > 0) {
            "${days}일"
        } else {
            ""
        }

        // 연도와 월이 모두 있는 경우
        if (years > 0 && months > 0) {
            return "$yearStr $monthStr $dayStr"
        }

        // 연도만 있는 경우
        if (years > 0 && months == 0) {
            return "$yearStr $dayStr"
        }

        // 월만 있는 경우
        if (years == 0 && months > 0) {
            return "$monthStr $dayStr"
        }

        // 그 외의 경우 (연도와 월이 모두 없는 경우)
        return dayStr
    }


    suspend fun loadPetInfo():Boolean{

        val apiService = RetrofitClientServer.instance

        val data = MyPetListReq(MySharedPreference.getUserId())

        val call = apiService.myPetList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<MyPetListRes>{
                override fun onResponse(call: Call<MyPetListRes>, response: Response<MyPetListRes>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.petDetailData?.let {
                            if(body.petDetailData.isEmpty()){
                                _petInfo.value= arrayListOf(emptyPet)
                                //updatePetInfo(arrayListOf(emptyPet))
                            }else{
                                _petInfo.value=body.petDetailData
                                //updatePetInfo(body.petDetailData)
                            }
                            Log.d("LOG",_petInfo.value.toString())
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }
                override fun onFailure(call: Call<MyPetListRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun loadCurrentPetInfo():Boolean{

        val apiService = RetrofitClientServer.instance

        val data = MyPetListReq(MySharedPreference.getUserId())

        val call = apiService.myPetListCurrent(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object :Callback<CurrentPetRes>{
                override fun onResponse(
                    call: Call<CurrentPetRes>,
                    response: Response<CurrentPetRes>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if(it.data.isEmpty()){
                                _currentPetInfo.value= arrayListOf(emptyCurrentPet)
                            }else{
                                _currentPetInfo.value=it.data
                            }
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<CurrentPetRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    private val _registedAppKey = MutableStateFlow<String?>("")

    suspend fun sendRFToken():Boolean{
        val apiService = RetrofitClientServer.instance

        val refreshToken = net.pettip.singleton.MySharedPreference.getRefreshToken()

        val call = apiService.sendRefreshToken(RefreshToken(refreshToken = refreshToken?:"", appTypNm = Build.MODEL.toString()))
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<RefreshRes>{
                override fun onResponse(call: Call<RefreshRes>, response: Response<RefreshRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (it.statusCode==200){
                                //G.accessToken = it.data?.accessToken
                                //G.refreshToken = it.data?.refreshToken
                                //G.userId = it.data?.userId ?: ""
                                //G.userNickName = it.data?.nckNm ?:""
                                //G.userEmail = it.data?.email ?: ""

                                _registedAppKey.value = it.data?.appKeyVl ?: ""
                                trmnlMng()

                                _nickName.value = it.data?.nckNm ?: ""

                                MySharedPreference.setAccessToken(it.data?.accessToken?:"")
                                MySharedPreference.setRefreshToken(it.data?.refreshToken ?:"")
                                MySharedPreference.setUserId(it.data?.userId ?:"")
                                MySharedPreference.setUserNickName(it.data?.nckNm?:"")
                                MySharedPreference.setUserEmail(it.data?.email?:"")

                                continuation.resume(true)
                            }else{

                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<RefreshRes>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }

    fun trmnlMng(){
        val appkey = MySharedPreference.getFcmToken()
        // 등록된 토큰과 현재 디바이스 토큰이 다르면 업데이트
        if ( appkey != _registedAppKey.value || _registedAppKey.value.isNullOrBlank()  ){
            val apiService = RetrofitClientServer.instance

            val data = TrmnlMngReq(
                appKey = appkey,
                appOs = "001",
                appTypNm = Build.MODEL.toString()
            )

            val call = apiService.trmnlMng(data)
            call.enqueue(object :Callback<commonRes>{
                override fun onResponse(call: Call<commonRes>, response: Response<commonRes>) {
                    Log.d("TRM","등록성공")
                }

                override fun onFailure(call: Call<commonRes>, t: Throwable) {
                    Log.d("TRM","등록실패")
                }

            })
        }

    }

    suspend fun getWeekRecord(ownrPetUnqNo: String, searchDay: String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = WeekRecordReq(ownrPetUnqNo, searchDay)

        val call=apiService.getWeekRecord(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<WeekRecordRes>{
                override fun onResponse(
                    call: Call<WeekRecordRes>,
                    response: Response<WeekRecordRes>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode==200){
                                _weekRecord.value=body.data
                                continuation.resume(true)
                            }else{
                                _weekRecord.value = null
                                continuation.resume(false)
                            }
                        }
                    }else{
                        _weekRecord.value = null
                        continuation.resume(false)
                    }
                }
                override fun onFailure(call: Call<WeekRecordRes>, t: Throwable) {
                    _weekRecord.value = null
                    continuation.resume(false)
                }
            })
        }
    }



    val emptyCurrentPet = CurrentPetData(
        age = "0살",
        petNm = "펫을 등록해주세요",
        ownrPetUnqNo = "",
        petKindNm = "",
        petRprsImgAddr = "",
        sexTypNm = "모름",
        wghtVl =0.0f,
        petRelUnqNo = 0,
        mngrType = "M"
    )

    fun clear(){
        _currentTab.value = "스토리"
        _profilePet.value = emptyPet
        _pushData.value = null
        _nickName.value = ""
        _init.value = true
        _moreStoryClick.value = null
        _toStory.value = false
        _weekRecord.value = null
        _petInfo.value = emptyList()
        _currentPetInfo.value = emptyList()
        _selectPet.value = null
        _selectPetTemp.value = null
        _selectPetMulti.value = mutableListOf()
    }
}

